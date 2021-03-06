package cn.cocowwy.suona.autoconfiguration;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.component.SuonaHelp;
import cn.cocowwy.suona.handler.IMethodHandler;
import cn.cocowwy.suona.handler.SuonaExecutor;
import cn.cocowwy.suona.handler.SuonaMethodWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author cocowwy.cn
 * @create 2022-04-04-17:10
 */
@EnableConfigurationProperties({SuonaProperties.class})
@ComponentScan({"cn.cocowwy.suona.component", "cn.cocowwy.suona.component.communication"})
@ConditionalOnProperty(name = "suona.enable", havingValue = "true")
public class SuonaAutoConfiguration implements SmartInitializingSingleton {
    private static final Log LOG = LogFactory.getLog(SuonaAutoConfiguration.class);
    private final ApplicationContext applicationContext;
    private final SuonaHelp suonaHelp;

    public SuonaAutoConfiguration(ApplicationContext applicationContext, SuonaHelp suonaHelp) {
        this.applicationContext = applicationContext;
        this.suonaHelp = suonaHelp;
        try {
            boolean isWeb = ClassUtils
                    .resolveClassName("org.springframework.web.context.WebApplicationContext",
                            null).isAssignableFrom(applicationContext.getClass());
            if (!isWeb) {
                throw new IllegalAccessException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Not web environment, Suona cannot be started");
        }
        LOG.debug("Environment check passed");
    }

    /**
     * ???????????? bean ????????????????????????????????? ?????? ??????
     */
    @Override
    public void afterSingletonsInstantiated() {
        if (applicationContext == null) {
            return;
        }

        String[] beanNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Map<Method, Suona> annotatedMethods = null;
            try {
                // ???????????? bean ???????????????????????????
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        (MethodIntrospector.MetadataLookup<Suona>) method
                                -> AnnotatedElementUtils.findMergedAnnotation(method, Suona.class));
            } catch (Throwable ex) {
                LOG.error("Suona method collection method error for bean[" + beanName + "].", ex);
            }

            if (annotatedMethods == null || annotatedMethods.isEmpty()) {
                continue;
            }

            // register
            for (Map.Entry<Method, Suona> methodSuonaEntry : annotatedMethods.entrySet()) {
                Method method = methodSuonaEntry.getKey();
                Suona sna = methodSuonaEntry.getValue();

                String name = StringUtils.isEmpty(sna.name())
                        ? suonaHelp.buildSuonaName(beanName, method.getName()) : sna.name();

                method.setAccessible(true);

                IMethodHandler exist = SuonaExecutor.registMethod(name, new SuonaMethodWrapper(bean, method, sna));
                if (Objects.nonNull(exist)) {
                    throw new RuntimeException("There is the same Suona named [" + name + "]");
                }
            }
        }

        LOG.info("Suona registration is complete");
    }
}
