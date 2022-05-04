package cn.cocowwy.suonatestserver1.test2;

import cn.cocowwy.suona.annotation.Suona;
import org.springframework.stereotype.Service;

/**
 * @author Cocowwy
 * @create 2022-05-05-21:58
 */
@Service
public class TestService {
    @Suona
    public void sayHi(){
        System.out.println("node 1 ---->");
    }
}
