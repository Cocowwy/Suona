package cn.cocowwy.suona.handler;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Suona 方法调度中心
 * Dispatch Center
 *
 * @author Cocowwy
 * @create 2022-04-04-21:37
 */
public class SuonaExecutor {
    private static final Log logger = LogFactory.getLog(SuonaExecutor.class);
    private static final ConcurrentMap<String, IMethodHandler> methodRepository = new ConcurrentHashMap<>(16);

    private SuonaExecutor() {
    }

    public static IMethodHandler registMethod(String name, IMethodHandler method) {
        System.out.println(name + "  register~");
        return methodRepository.put(name, method);
    }

    public static void execute(String name) throws Exception {
        IMethodHandler method = methodRepository.get(name);

        if (method == null) {
            logger.error("method [" + name + "] unregistered");
            return;
        }

        method.execute();
        logger.info("method [" + name + "] execution succeed");
    }

    public static Boolean had(String name) {
        return methodRepository.containsKey(name);
    }

}
