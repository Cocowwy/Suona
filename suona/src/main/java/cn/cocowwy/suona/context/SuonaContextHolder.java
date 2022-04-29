package cn.cocowwy.suona.context;

/**
 * 使用 SKIP 标记当前是执行者 而不是发起者 解决网站循环调用的问题
 * @author Cocowwy
 * @create 2022-04-04-21:26
 */
public class SuonaContextHolder {
    public static final ThreadLocal<Boolean> SKIP = new ThreadLocal<>();

    static {
        SKIP.set(Boolean.FALSE);
    }

    public static void skip() {
        SKIP.set(Boolean.TRUE);
    }

    public static Boolean call() {
        return !SKIP.get();
    }

    public static void clean(){
        SKIP.remove();
    }
}
