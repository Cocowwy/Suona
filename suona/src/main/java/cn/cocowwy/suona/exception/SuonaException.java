package cn.cocowwy.suona.exception;

/**
 * Suona异常基类
 * @author cocowwy.cn
 * @create 2022-05-05-11:45
 */
public class SuonaException extends RuntimeException{
    public SuonaException() {
        super();
    }

    public SuonaException(String message) {
        super(message);
    }
}
