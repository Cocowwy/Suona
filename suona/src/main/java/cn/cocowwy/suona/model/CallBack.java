package cn.cocowwy.suona.model;

/**
 * @author Cocowwy
 * @create 2022-04-04-21:09
 */
public class CallBack {
    private Boolean success;

    private CallBack(Boolean success) {
        this.success = success;
    }

    public static CallBack oK() {
        return new CallBack(Boolean.TRUE);
    }

    public static CallBack notOk() {
        return new CallBack(Boolean.FALSE);
    }

    public boolean isOk() {
        return this.success;
    }
}
