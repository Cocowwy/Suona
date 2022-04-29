package cn.cocowwy.suona.model;

/**
 * @author Cocowwy
 * @create 2022-04-04-21:09
 */
public class CallMethods {
    /**
     * call的方法名
     */
    private String name;
    /**
     * 消息发起者 url
     */
    private String url;
    /**
     * 集群服务名
     */
    private String serverName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public CallMethods(String name, String serverName) {
        this.name = name;
        this.serverName = serverName;
    }
}
