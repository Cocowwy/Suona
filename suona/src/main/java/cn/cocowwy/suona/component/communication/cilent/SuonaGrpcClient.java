package cn.cocowwy.suona.component.communication.cilent;

import cn.cocowwy.suona.annotation.Suona;
import cn.cocowwy.suona.component.communication.SuonaClient;
import cn.cocowwy.suona.enums.CommunicateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

/**
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
@Component
public class SuonaGrpcClient implements SuonaClient {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void callOthers(Suona suona, String name) {

    }

    @Override
    public void asyncCallOthers(Suona suona, String name) {

    }

    @Override
    public CommunicateEnum communicationMode() {
        return CommunicateEnum.Grpc;
    }
}
