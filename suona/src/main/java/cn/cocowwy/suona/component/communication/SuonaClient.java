package cn.cocowwy.suona.component.communication;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.handler.SuonaExecutor;
import cn.cocowwy.suona.model.CallBack;
import cn.cocowwy.suona.model.CallMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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
    private static final Log logger = LogFactory.getLog(SuonaClient.class);
    private static final String path = "/%s/suona/call";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String serverName;
    private final String api;
    private static HttpHeaders headers;

    static {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    public SuonaClient(@Value("${server.servlet.context-path}") String prefix,
                       @Value("${spring.application.name}") String serverName,
                       RestTemplate restTemplate, ObjectMapper objectMapper) {
        api = String.format(path, prefix);
        this.serverName = serverName;
        this.restTemplate = restTemplate != null ? restTemplate : new RestTemplate();
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    public void callOthers(Suona suona) {
        String name = suona.name();

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

        // todo custom urls

        List<String> urls = discoveryClient.getInstances(serverName)
                .stream()
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .collect(Collectors.toList());

        HttpEntity<String> request = new HttpEntity<>(msg, headers);

        for (String url : urls) {
            ResponseEntity<CallBack> exchange = restTemplate
                    .exchange(url + api, HttpMethod.POST, request, CallBack.class);

            CallBack callBack = exchange.getBody();

            if (!callBack.isOk()) {
                logger.error("Suona call method [" + name + "] for url [" + url + "] is error");
            }

        }

        logger.info("Suona call method [" + name + "] complete");
    }
}
