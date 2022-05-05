package cn.cocowwy.suona.component.communication;


import cn.cocowwy.suona.context.SuonaContextHolder;
import cn.cocowwy.suona.handler.SuonaExecutor;
import cn.cocowwy.suona.model.CallBack;
import cn.cocowwy.suona.model.CallMethods;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 节点通讯：
 * V1：采用内嵌 HTTP 接口的形式进行
 *
 * @author cocowwy.cn
 * @create 2022-04-04-21:01
 */
@RestController
@RequestMapping("suona")
public class SuonaReceive {
    @PostMapping("call")
    public CallBack call(@RequestBody CallMethods call) {

        try {
            suonaBiz(call);
        } catch (Exception exception) {
            //todo fixme

            return new CallBack(Boolean.FALSE);
        } finally {
            SuonaContextHolder.clean();
        }

        return new CallBack(Boolean.TRUE);
    }

    /**
     * 使用 ThreadLocal进行线程标记，使其不会被切面二次调用
     * Suona内部通讯，即走 HTTP 请求过来的接口 进行标记
     * 在线程上下文环境中，如果有标记，则进行调用，同时并且清除标记，
     * 防止栈递归调用（切面逻辑里面）
     *
     * @param call
     */
    private void suonaBiz(CallMethods call) throws Exception {
        SuonaContextHolder.label();
        SuonaExecutor.execute(call.getName());
    }
}
