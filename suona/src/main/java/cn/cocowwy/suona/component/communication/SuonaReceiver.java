package cn.cocowwy.suona.component.communication;

import cn.cocowwy.suona.model.CallBack;
import cn.cocowwy.suona.model.CallMethods;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 定义接受者行为
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
public interface SuonaReceiver extends SuonaCommunicate {
    CallBack call(@RequestBody CallMethods call);
}
