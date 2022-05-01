package cn.cocowwy.suonatestserver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SuonaTestServer2Application {

    public static void main(String[] args) {
        SpringApplication.run(SuonaTestServer2Application.class, args);
    }

}
