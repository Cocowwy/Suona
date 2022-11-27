package cn.cocowwy.suona.exception;

/**
 * Suona通讯异常
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
public class SuonaCommunicationException extends SuonaException{
    public SuonaCommunicationException() {
        super();
    }

    public SuonaCommunicationException(String message) {
        super(message);
    }
}
