package cn.cocowwy.suona.context;

/**
 * 使用 SKIP 标记当前执行者不进行分发操作
 * @author Cocowwy
 * @create 2022-04-04-21:26
 */
public class SuonaContextHolder {
    public static final ThreadLocal<Object> SKIP = new ThreadLocal<>();

    public static void label() {
        SKIP.set(new Object());
    }

    public static Boolean skip() {
        return SKIP.get() != null;
    }

    public static void clean() {
        SKIP.remove();
    }
}
