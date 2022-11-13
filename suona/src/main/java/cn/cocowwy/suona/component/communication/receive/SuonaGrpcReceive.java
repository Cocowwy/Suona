package cn.cocowwy.suona.component.communication.receive;

import cn.cocowwy.suona.component.communication.SuonaReceiver;
import cn.cocowwy.suona.enums.CommunicateEnum;
import cn.cocowwy.suona.model.CallBack;
import cn.cocowwy.suona.model.CallMethods;
import org.springframework.stereotype.Component;

/**
 * 节点通讯：
 * 采用内嵌 gRPC 进行通讯
 *
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
@Component
public class SuonaGrpcReceive implements SuonaReceiver {
    @Override
    public CallBack call(CallMethods call) {
        return null;
    }

    @Override
    public CommunicateEnum communicationMode() {
        return CommunicateEnum.Grpc;
    }
}
