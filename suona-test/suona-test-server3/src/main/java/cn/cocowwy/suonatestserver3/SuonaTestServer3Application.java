package cn.cocowwy.suonatestserver3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SuonaTestServer3Application {

    public static void main(String[] args) {
        SpringApplication.run(SuonaTestServer3Application.class, args);
    }

}
