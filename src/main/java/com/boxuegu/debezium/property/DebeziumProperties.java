package com.boxuegu.debezium.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * debezium属性
 *
 * @author lsx
 * @date 2021/11/1 16:25
 */
@Data
@ConfigurationProperties(prefix = "debezium")
public class DebeziumProperties {
    /**
     * 数据库配置
     */
    private Database database;
    /**
     * cdc设置
     */
    private ChangeDataCapture cdc;

    @Setter
    @Getter
    @ToString
   public static class Database{
       /**
        * MySQL服务器或集群的逻辑名称, 自己随便起个有意义的名字，后面在数据中好辨识
        */
       private String serverName;
       /**
        * 数据库mysql.cnf配置的server-id
        */
       private String serverId;
       /**
        * 数据库地址
        */
       private String ip;
       /**
        * 数据库端口
        */
       private String port;
       /**
        * 数据库用户名，需要注意此用户要有对应的权限
        */
       private String user;
       /**
        * 数据库密码
        */
       private String password;

   }

   @Setter
   @Getter
   @ToString
   public static class ChangeDataCapture{
       /**
        * 要监听的数据库
        */
       private String dbIncludeList;
       /**
        * 要监听的表，多个用都好隔开
        */
       private String tableIncludeList;
       /**
        * 偏移量持久化文件路径 默认/tmp/offsets.dat
        */
       private String offsetFile;
       /**
        * 历史变更记录存储位置
        */
       private String historyFile;
   }

}
