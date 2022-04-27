package cn.cocowwy.suona.component;

import cn.cocowwy.suona.annotation.Suona;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 对 Suona 标注的方法进行代理，执行集群方法的同步
 * @author cocowwy.cn
 * @create 2022-04-04-9:48
 */
@Aspect
@Component
public class SuonaAwareAspect {
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
    //TODO
//      方法执行的时候，
//      但是存在一个情况：
//      A 调用触发的时候 B ，C 节点同时触发，但是此时 B ，C
//     的该方法都同时触发到了，此时也会走AOP 代理   就会形成网状的循环
//     得考虑一下 如何防止这种情况的发生
    // 考虑 携带一个全局标识ID 如果ID相同则直接返回并return  或则判断走controller进来的请求直接不走这个AOP
    @Around("pointcut4Suona()&&@annotation(suona)")
    public Object around(ProceedingJoinPoint point, Suona suona) throws Throwable {
        point.proceed();
        return point.proceed();
    }
}
