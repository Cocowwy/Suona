package cn.cocowwy.suona.component.communication;

import cn.cocowwy.suona.annotation.Suona;
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
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cocowwy
 * @create 2022-04-04-22:07
 */
@Component
public class SuonaClient {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Environment environment;
    private static final Log logger = LogFactory.getLog(SuonaClient.class);
    private static final String path = "/%s/suona/call";
    private final static RestTemplate restTemplate = new RestTemplate();
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static HttpHeaders headers = new HttpHeaders();
    private String serverName;
    private String api;

    static {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    @PostConstruct
    public void initPath() {
        String prefix = environment.getProperty("server.servlet.context-path");
        api = String.format(path, prefix == null ? "" : prefix);
        this.serverName = environment.getProperty("spring.application.name");
    }

    public void callOthers(Suona suona, String name) {
        if (StringUtils.isEmpty(name) || !SuonaExecutor.had(name)) {
            logger.error("method [" + name + "] Invalid");
            return;
        }

        String msg = null;
        try {
            msg = objectMapper.writeValueAsString(new CallMethods(name, serverName));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<String> urls = suona.url().length > 0
                ? Arrays.asList(suona.url()) :
                discoveryClient.getInstances(serverName)
                        .stream()
                        .map(ServiceInstance::getUri)
                        .map(URI::toString)
                        .collect(Collectors.toList());

        HttpEntity<String> request = new HttpEntity<>(msg, headers);

        for (String url : urls) {
            ResponseEntity<String> exchange = restTemplate
                    .exchange(url + api, HttpMethod.POST, request, String.class);

            CallBack callBack = null;

            try {
                callBack = objectMapper.readValue(exchange.getBody(), new TypeReference<CallBack>() {});
            } catch (JsonProcessingException e) {
                logger.error("Serialization error ["+request.getBody()+"]");
                e.printStackTrace();
            }

            // exclue call self
            if (callBack == null) {
                continue;
            }

            if (!callBack.isOk()) {
                logger.error("Suona call method [" + name + "] for url [" + url + "] is error");
            }
        }

        logger.info("Suona call method [" + name + "] complete");
    }
}
