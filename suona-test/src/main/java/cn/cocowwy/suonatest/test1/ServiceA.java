package cn.cocowwy.suonatest.test1;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.component.SuonaExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Cocowwy
 * @create 2022-04-04-22:02
 */

@Service
public class ServiceA {
    @Suona
    public void sayA() throws Exception {
        System.out.println("test1 ServiceA sayA");
        SuonaExecutor.execute("SuonaTest@serviceA@sayA");
    }

    public void sayB() {
        System.out.println("test1 ServiceA sayB");
    }
}

class ServiceB {
    @Suona
    public void sayA() {
        System.out.println("test1 ServiceB sayA");
    }

    public void sayB() {
        System.out.println("test1 ServiceB sayB");
    }
}

@Component
class ServiceC {
    @Suona
    public void sayA() {
        System.out.println("test1 ServiceC sayA");
    }

    public void sayB() {
        System.out.println("test1 ServiceC sayB");
    }
}

@Component
class Start implements ApplicationRunner {
    @Autowired
    private ServiceA serviceA;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 测试Suona AOP 生效
        serviceA.sayA();
    }
}

