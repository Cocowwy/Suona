package cn.cocowwy.suona.autoconfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.ClassUtils;

/**
 * @author cocowwy.cn
 * @create 2022-04-04-17:10
 */
@EnableConfigurationProperties({SuonaProperties.class})
@ComponentScan("cn.cocowwy.suona.component")
@ConditionalOnClass(DiscoveryClient.class)
public class SuonaAutoConfiguration implements SmartInitializingSingleton {
    private static final Log logger = LogFactory.getLog(SuonaAutoConfiguration.class);
    private final ApplicationContext applicationContext;

    public SuonaAutoConfiguration(ApplicationContext applicationContext, SuonaProperties suonaProperties) {
        this.applicationContext = applicationContext;
        try {
            boolean isWeb = ClassUtils.resolveClassName("org.springframework.web.context.WebApplicationContext",
                    null).isAssignableFrom(applicationContext.getClass());
            if (!isWeb) {
                throw new IllegalAccessException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Not web environment, Suona cannot be started");
        }
        logger.debug("Environment check passed");
    }

    /**
     * 所有单例 bean 创建完成之后，执行方法 注册 逻辑
     */
    @Override
    public void afterSingletonsInstantiated() {
        // 注册方法

    }
}
