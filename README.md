# Suona 🎺
```Suona```**一个半去中心化的集群方法同步执行框架**

## 说明
项目借鉴XXL-job源码，以及SpingCloud的服务注册，实现单节点方法被调用时唤醒集群其余节点的方法，以达到去中心化的广播效果

## 场景
情景一：比如在使用 ```caffine``` / ```hashMap``` 作为服务节点的内存缓存的时候，这是有缺点的，因为在集群内，如果需要进行缓存清理操作，是需要对每个节点进行缓存清理的    
情景二：类似于情景一，需要该服务的集群每个节点都执行某方法  

如果需要解决这种方案，就得 ```MQ``` 等中间件来实现广播的操作，那么如果中间件挂了，或者是不想那么麻烦来依靠外部的组件来进行怎么办呢？  

那么，可以尝试使用 ```@Suona``` ，旨在能够让开发者在单一节点的方法被调用的时候，触发其他节点的同步进行调用，来模拟广播的操作，而开发者**仅需要使用一个注解来标记这个方法**，即可达到拆箱即用的效果  
ps: 当前版本仅支持无参方法的使用

## 使用方式
你只需要这样使用它：
```
@Service
public class ServiceA {
    @Suona
    public void sayA() {
        // do sth.
    }
}
```
这样，当集群内任意节点的方法被调用时，其余节点也会同时进行调用  
当前1.0.0版本仅支持无参方法的调用集群调用，下个版本将进行优化迭代

## 使用环境
|  TECHNOLOGY   | ENV  |  
|  ----  | ----  |
|  JDK  | 1.8+  |  
|  SpringCloud  | discovery |


## 注意
**如果使用了权限框架或者是自定义了web拦截器的需要注意一下：**  
**由于Suona集群通讯采用内嵌的web接口进行，所以需要对该web请求进行放行**： ```/suona/call```

## 稳定版
```
<dependency>
    <groupId>cn.cocowwy</groupId>
    <artifactId>suona</artifactId>
    <version>1.0.0</version>
</dependency>
```


## 快照版本见下，发布最新的代码
```
<dependency>
    <groupId>cn.cocowwy</groupId>
    <artifactId>suona</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
<repositories>
    <repository>
        <id>snapshots</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
