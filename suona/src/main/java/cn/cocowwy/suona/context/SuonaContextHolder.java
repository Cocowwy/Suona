package cn.cocowwy.suona.context;

/**
 * 使用 SKIP 标记当前执行者不进行分发操作
 * @author Cocowwy
 * @create 2022-04-04-21:26
 */
public class SuonaContextHolder {
    public static final ThreadLocal<SuonaThreadContext> SUONA_CONTET = new ThreadLocal<>();

    public static void get() {
        SUONA_CONTET.get();
    }

    public static void label() {
        SUONA_CONTET.set(new SuonaThreadContext(true));
    }

    /**
     * 是否执行方法
     * @return
     */
    public static Boolean doSuonaMethod() {
        if (SUONA_CONTET.get() == null) {
            // 发送者走切面 故需标记为发起者
            SUONA_CONTET.set(new SuonaThreadContext(false));
        }
        return Boolean.TRUE;
    }

    /**
     * 是否执行唤醒其他节点方法
     * @return
     */
    public static Boolean doCallOthers() {
        return !SUONA_CONTET.get().getAccept();
    }

    public static void clean() {
        SUONA_CONTET.remove();
    }
}
