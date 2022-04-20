package cn.cocowwy.suona.autoconfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * @author cocowwy.cn
 * @create 2022-04-04-17:10
 */
@EnableConfigurationProperties({SuonaProperties.class})
public class SuonaAutoConfiguration {
    private static final Log logger = LogFactory.getLog(SuonaAutoConfiguration.class);

    public SuonaAutoConfiguration(ApplicationContext applicationContext, SuonaProperties suonaProperties) {

        boolean isWeb = ClassUtils.resolveClassName("org.springframework.web.context.WebApplicationContext",
                (ClassLoader) null).isAssignableFrom(applicationContext.getClass());

        if (!isWeb) {
            logger.error("当前不是web环境，suona无法启动");
        }
    }

}
