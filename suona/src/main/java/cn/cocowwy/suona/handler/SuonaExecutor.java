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
public final class SuonaExecutor {
    private static final Log LOG = LogFactory.getLog(SuonaExecutor.class);
    private static final ConcurrentMap<String, IMethodHandler> METHOD_REPOSITORY = new ConcurrentHashMap<>(16);

    private SuonaExecutor() {
    }

    public static IMethodHandler registMethod(String name, IMethodHandler method) {
        return METHOD_REPOSITORY.put(name, method);
    }

    public static void execute(String name) throws Exception {
        IMethodHandler method = METHOD_REPOSITORY.get(name);

        if (method == null) {
            LOG.error("method [" + name + "] unregistered");
            return;
        }

        method.execute();
        LOG.info("Method [" + name + "] execution succeed as accepter");
    }

    public static Boolean had(String name) {
        return METHOD_REPOSITORY.containsKey(name);
    }

}
