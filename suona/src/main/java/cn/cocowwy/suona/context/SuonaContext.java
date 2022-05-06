package cn.cocowwy.suona.context;

import cn.cocowwy.suona.component.SuonaAwareAspect;

/**
 * Suona线程变量
 * 用于保存当前服务是发起者还是接收者
 * 并且解决被{@link SuonaAwareAspect}代理的方法栈递归调用的问题
 * @author Cocowwy
 * @create 2022-05-05-20:28
 */
public class SuonaContext {
    /**
     * 是否接收者
     *      默认为发起者，即 false
     *      该值会在接受者接收消息时候修改为 true
     *      用于解决接受者进行消息发布操作
     */
    private Boolean accept;

    /**
     * 线程上下文中，方法被调用的次数
     *      用于解决方法经过{@link SuonaAwareAspect}切面多次调用
     */
    private Integer times;

    public SuonaContext(Boolean accept) {
        this.accept = accept;
        this.times = 0;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public Integer getTimes() {
        return times;
    }

    public void add() {
        this.times += 1;
    }
}
