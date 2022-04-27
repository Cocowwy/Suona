package cn.cocowwy.suona.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Cocowwy
 * @create 2022-04-04-21:10
 */
@Component
public class SuonaHelp {
    private final String serverName;

    public SuonaHelp(@Value("spring.application.name") String serverName) {
        this.serverName = StringUtils.isEmpty(serverName) ? "" : serverName;
    }

    /**
     * 服务名@全限定类名@方法名
     * @return
     */
    public String buildSuonaName(String className, String methodName) {
        return String.format("%s@%s@%s", serverName, className, methodName);
    }
}
