package cn.cocowwy.suona.component;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 对 Suona 标注的方法进行代理，执行集群方法的同步
 * @author cocowwy.cn
 * @create 2022-04-04-9:48
 */
@Aspect
@Component
public class SuonaAwareAspect {
}
