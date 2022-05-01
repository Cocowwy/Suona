package cn.cocowwy.suonatestserver1.test2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Cocowwy
 * @create 2022-05-05-23:21
 */
@RestController
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 测试 cluster 地址列表
     * http://localhost:8080/findServers
     * 192.168.1.14 | 8081 | http://192.168.1.14:8081
     * 192.168.1.14 | 8080 | http://192.168.1.14:8080
     */
    @GetMapping("/findServers")
    public void findServers() {
        List<ServiceInstance> server1 = discoveryClient.getInstances("server1");
        server1.forEach(it -> {
            System.out.println(String.format("%s | %s | %s", it.getHost(), it.getPort(), it.getUri()));

        });
    }

}
