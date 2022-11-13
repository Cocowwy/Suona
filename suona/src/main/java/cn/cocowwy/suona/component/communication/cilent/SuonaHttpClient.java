package cn.cocowwy.suona.component.communication.cilent;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.component.communication.SuonaClient;
import cn.cocowwy.suona.enums.CommunicateEnum;
import cn.cocowwy.suona.handler.SuonaExecutor;
import cn.cocowwy.suona.model.CallBack;
import cn.cocowwy.suona.model.CallMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Suona 通讯采用 内嵌HTTP接口的方式
 * @author Cocowwy
 * @create 2022-04-04-22:07
 */
@Component
public class SuonaHttpClient implements SuonaClient {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Environment environment;
    private static final Log LOGGER = LogFactory.getLog(SuonaClient.class);
    private static final String PATH = "/%s/suona/call";
    private static final String URL_HTTP_PREFIX = "http://";
    private static final String URL_HTTPS_PREFIX = "https://";
    private  final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static
    final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HttpHeaders HEAEDERS = new HttpHeaders();
    private String serverName;
    private String localUrl;
    private String api;
    private String port;

    static {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        HEAEDERS.setContentType(type);
        HEAEDERS.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    @PostConstruct
    public void initPath() throws UnknownHostException {
        String prefix = environment.getProperty("server.servlet.context-path");
        api = String.format(PATH, prefix == null ? "" : prefix);
        this.serverName = environment.getProperty("spring.application.name");
        this.port = environment.getProperty("server.port") == null ? "8080" : environment.getProperty("server.port");
        this.localUrl = String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), this.port);
    }

    public void callOthers(Suona suona, String name) {
        if (StringUtils.isEmpty(name) || !SuonaExecutor.had(name)) {
            LOGGER.error("method [" + name + "] Invalid");
            return;
        }

        String msg = null;
        try {
            msg = OBJECT_MAPPER.writeValueAsString(new CallMethods(name, serverName));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // get all ips in this server
        List<String> urls = suona.url().length > 0
                ? Arrays.asList(suona.url())
                : discoveryClient.getInstances(serverName)
                .stream()
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .collect(Collectors.toList());

        // remove local 本地IP不进行节点方法的调用
        LOGGER.info("Suona find ip list is:[" + Arrays.toString(urls.toArray()) + "]");
        urls = urls.stream().filter(u -> !u.contains(localUrl)).collect(Collectors.toList());
        LOGGER.info("Remove local , this ip list:[" + Arrays.toString(urls.toArray()) + "] will be call [" + name + "]");
        HttpEntity<String> request = new HttpEntity<>(msg, HEAEDERS);
        for (String url : urls) {

            // fix: java.net.URISyntaxException
            if (!url.startsWith(URL_HTTP_PREFIX) && !url.startsWith(URL_HTTPS_PREFIX)) {
                url = URL_HTTP_PREFIX + url;
            }

            ResponseEntity<String> exchange = REST_TEMPLATE
                    .exchange(url + api, HttpMethod.POST, request, String.class);

            CallBack callBack = null;

            try {
                callBack = OBJECT_MAPPER.readValue(exchange.getBody(), new TypeReference<CallBack>() {
                });
            } catch (JsonProcessingException e) {
                LOGGER.error("Serialization error [" + request.getBody() + "]");
                e.printStackTrace();
            }

            //  exclude call self
            if (callBack == null) {
                continue;
            }

            if (!callBack.getSuccess()) {
                LOGGER.error("Suona call method [" + name + "] for url [" + url + "] is error");
            }
        }
        LOGGER.info("Suona call method [" + name + "] complete");
    }

    public void asyncCallOthers(Suona suona, String name) {
        new Thread(() -> this.callOthers(suona, name)).start();
    }

    @Override
    public CommunicateEnum communicationMode() {
        return CommunicateEnum.Http;
    }
}
