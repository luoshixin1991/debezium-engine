package com.boxuegu.debezium.property;

import com.boxuegu.debezium.model.dto.RocketMetaDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rocketMQ服务器信息
 *
 * @author lsx
 * @date 2021/11/1 11:32
 */
@Data
@ConfigurationProperties(prefix = "rocket")
public class RocketProperties {
    /**
     * rocketMQ 地址
     */
    private String nameServerAddr;
    private String accessKey;
    private String secretKey;
    /**
     * 发送消息的超时时间
     */
    private String sendMsgTimeoutMillis;
    /**
     * topic数据
     */
    private Meta meta;

    @Setter
    @Getter
    @ToString
    public static class Meta {
        /**
         * 数据库cdc消息处理的topic
         */
        private RocketMetaDTO cdc;
    }

}
