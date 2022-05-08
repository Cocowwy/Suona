package cn.cocowwy.suonatestserver3.test;

import cn.cocowwy.suona.annotation.Suona;
import org.springframework.stereotype.Service;

/**
 * @author Cocowwy
 * @create 2022-05-05-19:05
 */
@Service
public class TestService {
    @Suona
    public void sayHi() {
        System.out.println("Hello suona!");
    }

    @Suona
    public void syncSayHi() {
        new Thread(() -> {
            System.out.println("Hello suona!!!!!");
        }).start();
    }
}
