package cn.cocowwy.suona.component;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.component.communication.SuonaClient;
import cn.cocowwy.suona.component.communication.SuonaReceive;
import cn.cocowwy.suona.context.SuonaContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SuonaHelp suonaHelp;

    @Pointcut("@annotation(cn.cocowwy.suona.annotation.Suona)")
    private void pointcut4Suona() {
    }

    /**
     * 发起者先进行标记为发起者
     * 接受者在{@link SuonaReceive#suonaBiz(cn.cocowwy.suona.model.CallMethods)}已经被标记为接受者
     * 用来判断是否进行广播操作，防止接受者因为切面逻辑而被进行广播操作
     *
     * {@link cn.cocowwy.suona.context.SuonaContextHolder#doSuonaMethod()}
     * 通过该方法来判断方法是否继续执行，以为被 Suona方法执行后
     *
     * @param point
     * @param suona
     * @return
     * @throws Throwable
     */
    @Around("pointcut4Suona()&&@annotation(suona)")
    public Object around(ProceedingJoinPoint point, Suona suona) throws Throwable {
        // skip
        if (!SuonaContextHolder.doSuonaMethod()) {
            SuonaContextHolder.clean();
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

        // call others
        if (SuonaContextHolder.doCallOthers()) {
            suonaClient.callOthers(suona, name);
        }

        return proceed;
    }
}
