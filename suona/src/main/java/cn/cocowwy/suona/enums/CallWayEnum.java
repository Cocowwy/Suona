package cn.cocowwy.suona.enums;

/**
 * Suona 唤醒其它节点的操作
 * 是同步/异步进行
 *
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
public enum CallWayEnum {
    /**
     * 同步进行，默认
     */
    SYNC,
    /**
     * 异步进行
     */
    ASYNC;
}
