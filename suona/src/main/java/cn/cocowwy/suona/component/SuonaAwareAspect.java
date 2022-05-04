package cn.cocowwy.suona.component;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.component.communication.SuonaClient;
import cn.cocowwy.suona.context.SuonaContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用于感知被Suona标注的方法的调用，执行集群方法的同步
 * @author cocowwy.cn
 * @create 2022-04-04-9:48
 */
@Aspect
@Component
public class SuonaAwareAspect {
    @Autowired
    private SuonaClient suonaClient;
    @Autowired
    private ApplicationContext applicationContext;
    @AutoConfigureOrder
    private SuonaHelp suonaHelp;

    @Pointcut("@annotation(cn.cocowwy.suona.annotation.Suona)")
    private void pointcut4Suona() {
    }

    /**
     * 对标记的方法进行 注册
     *
     * @param point
     * @param suona
     * @return
     * @throws Throwable
     */
    @Around("pointcut4Suona()&&@annotation(suona)")
    public Object around(ProceedingJoinPoint point, Suona suona) throws Throwable {
        // skip
        if (!SuonaContextHolder.call()) {
            return null;
        }

        String name = suona.name();
        if (StringUtils.isEmpty(name)) {
            String methodName = point.getSignature().getName();
            String[] names = applicationContext.getBeanNamesForType(point.getTarget().getClass());
            String beanName = names[0];
            name = suonaHelp.buildSuonaName(beanName, methodName);
        }

        Object proceed = point.proceed();
        suonaClient.callOthers(suona, name);

        return proceed;
    }
}
