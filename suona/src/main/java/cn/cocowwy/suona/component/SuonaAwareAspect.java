package cn.cocowwy.suona.component;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.autoconfiguration.SuonaProperties;
import cn.cocowwy.suona.component.communication.SuonaClient;
import cn.cocowwy.suona.context.SuonaContextHolder;
import cn.cocowwy.suona.enums.CallWayEnum;
import cn.cocowwy.suona.enums.SpreadEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 用于感知被Suona标注的方法的调用
 * 执行集群方法的同步调用
 *
 * @author cocowwy.cn
 * @create 2022-04-04-9:48
 */
@Aspect
@Component
public class SuonaAwareAspect implements InitializingBean {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SuonaHelp suonaHelp;
    @Autowired
    private SuonaProperties suonaProperties;
    @Autowired
    private List<SuonaClient> communicationsModes;
    private SuonaClient routeSuonaClient;

    @Pointcut("@annotation(cn.cocowwy.suona.annotation.Suona)")
    private void pointcut4Suona() {
    }

    /**
     * 使用切面进行方法监听以及广播操作
     * 标记当前位接受者
     * {@link cn.cocowwy.suona.context.SuonaContextHolder#doSuonaMethod()}
     * 通过该方法来判断方法是否继续执行，以为被 Suona方法执行后
     */
    @Around("pointcut4Suona()&&@annotation(suona)")
    public Object around(ProceedingJoinPoint point, Suona suona) throws Throwable {
        // skip 对于发起者而言，AOP将要进行分发逻辑
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

        // call others 发送消息给集群的所有节点
        if (SuonaContextHolder.doCallOthers()
                && SpreadEnum.BROADCAST.equals(suona.spread())) {
            if (suona.callWay().equals(CallWayEnum.SYNC)) {
                routeSuonaClient.callOthers(suona, name);
            } else if (suona.callWay().equals(CallWayEnum.ASYNC)) {
                routeSuonaClient.asyncCallOthers(suona, name);
            } else {
                throw new RuntimeException("Error way to use Suona [" + suona.callWay() + "]");
            }
        }

        return proceed;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Optional<SuonaClient> optionalSuonaClient =
                communicationsModes.stream().filter(
                        it -> it.communicationMode().equals(suonaProperties.getCommunicate())
                ).findFirst();
        if (!optionalSuonaClient.isPresent()) {
            throw new IllegalAccessException("Illegal means of communication");
        }
        routeSuonaClient = optionalSuonaClient.get();
    }
}
