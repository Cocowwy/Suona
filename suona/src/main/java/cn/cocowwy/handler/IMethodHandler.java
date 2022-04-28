package cn.cocowwy.handler;

/**
 * @author Cocowwy
 * @create 2022-04-04-22:32
 */
public abstract class IMethodHandler {
    /**
     * 调度 执行
     *
     * @throws Exception
     */
    public abstract void execute() throws Exception;
}
