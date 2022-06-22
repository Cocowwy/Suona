package cn.cocowwy.suonatestserver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class SuonaTestServer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SuonaTestServer1Application.class, args);
    }

}
