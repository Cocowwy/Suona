package cn.cocowwy.suona.component.communication;

import cn.cocowwy.suona.enums.CommunicateEnum;

/**
 * 通讯方式
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
public interface SuonaCommunicate {
    /**
     * 通讯模式
     * Mode of communication
     *
     * @return CommunicateEnum
     */
    CommunicateEnum communicationMode();
}
