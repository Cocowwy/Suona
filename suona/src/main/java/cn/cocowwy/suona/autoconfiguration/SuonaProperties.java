package cn.cocowwy.suona.autoconfiguration;

import cn.cocowwy.suona.enums.SpreadEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author cocowwy.cn
 * @create 2022-04-04-17:10
 */
@ConfigurationProperties("suona")
public class SuonaProperties {
    private boolean enable = true;

    private String mode = SpreadEnum.BROADCAST.toString();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
