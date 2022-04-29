package cn.cocowwy.suona.handler;

import cn.cocowwy.suona.annotation.Suona;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Cocowwy
 * @create 2022-04-04-22:38
 */
public class SuonaMethodWrapper extends IMethodHandler {
    /**
     * Bean
     */
    private final Object target;
    /**
     * 方法
     */
    private final Method method;
    /**
     * Annotation
     */
    private final Suona suona;

    public SuonaMethodWrapper(Object target, Method method, Suona suona) {
        this.target = target;
        this.method = method;
        this.suona = suona;
    }

    /**
     * 调度 执行
     *
     * @throws Exception
     */
    @Override
    public void execute() throws InvocationTargetException, IllegalAccessException {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            // method-param can not be primitive-types
            method.invoke(target, new Object[paramTypes.length]);
        } else {
            method.invoke(target);
        }
    }
}
