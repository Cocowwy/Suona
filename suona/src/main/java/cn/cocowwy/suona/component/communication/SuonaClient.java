package cn.cocowwy.suona.component.communication;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.handler.SuonaExecutor;
import cn.cocowwy.suona.model.CallMethods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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
    private final String url;
    private final String serverName;
    private final RestTemplate restTemplate;

    public SuonaClient(@Value("${server.servlet.context-path}") String prefix,
                       @Value("${spring.application.name}") String serverName,
                       RestTemplate restTemplate) {
        url = String.format(path, prefix);
        this.serverName = serverName;
        this.restTemplate = restTemplate != null ? restTemplate : new RestTemplate();
    }

    public void callOthers(Suona suona) {
        String name = suona.name();

        if (StringUtils.isEmpty(name) || !SuonaExecutor.had(name)) {
            logger.error("method [" + name + "] Invalid");
            return;
        }

        new CallMethods(name, serverName);
        // todo HTTP

        // todo  log success info
    }
}
