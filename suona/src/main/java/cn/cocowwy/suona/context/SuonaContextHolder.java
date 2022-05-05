package cn.cocowwy.suona.context;

/**
 * 使用 SKIP 标记当前执行者不进行分发操作
 * @author Cocowwy
 * @create 2022-04-04-21:26
 */
public class SuonaContextHolder {
    public static final ThreadLocal<Integer> SKIP = new ThreadLocal<>();

    public static void label() {
        SKIP.set(1);
    }

    public static void add() {
        SKIP.set(SKIP.get() + 1);
    }

    public static Boolean skip() {
        // 标记开头（未在Suona执行逻辑中标记） 也指方法的第一次被调用
        if (SKIP.get() == null) {
            SKIP.set(1);
            return Boolean.FALSE;
        }

        return SKIP.get() > 1;
    }

    public static void clean() {
        SKIP.remove();
    }
}
