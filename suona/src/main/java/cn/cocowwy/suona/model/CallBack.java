package cn.cocowwy.suona.model;

import java.io.Serializable;

/**
 * @author Cocowwy
 * @create 2022-04-04-21:09
 */
public class CallBack implements Serializable {
    private Boolean success;

    public CallBack() {
    }

    public CallBack(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CallBack{"
                + "success="
                + success
                + '}';
    }
}
