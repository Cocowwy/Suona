package cn.cocowwy.suona.annotation;

import cn.cocowwy.suona.enums.SpreadEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cocowwy.cn
 * @create 2022-04-04-16:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface Suona {
    /**
     * 指定当前方法在节点维度的方法唯一标识名，
     * 一般情况下使用默认的生成策略即可，
     * 默认生成策略为：服务名@BeanName@方法名
     *
     * Default generation strategy is: service name @Bean Name @ method name
     */
    String name() default "";

    /**
     * 指定集群的 URL （IP+端口的形式）
     * 不传则使用的是注册中心所拉取到的当前服务集群的所有节点 URL 列表
     */
    String[] url() default {};

    /**
     * SpreadEnum.BROADCAST 模式  调用服务集群内所有节点的该方法
     * SpreadEnum.NONE      模式  不进行集群同步执行
     */
    SpreadEnum spread() default SpreadEnum.BROADCAST;
}
