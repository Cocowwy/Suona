package cn.cocowwy.suona.component.communication;

import cn.cocowwy.suona.annotation.Suona;

/**
 * 通过拉取服务IP的列表，来通讯该服务的所有节点
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
public interface SuonaClient extends SuonaCommunicate {
    /**
     * 同步通讯
     * @param suona
     * @param name
     */
    void callOthers(Suona suona, String name);

    /**
     * 异步通讯
     * @param suona
     * @param name
     */
    void asyncCallOthers(Suona suona, String name);
}
