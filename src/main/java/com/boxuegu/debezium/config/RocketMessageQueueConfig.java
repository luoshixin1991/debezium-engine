package com.boxuegu.debezium.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.boxuegu.debezium.property.RocketProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import java.util.Properties;

/**
 * rocketMQ topic 配置
 *
 * @author lsx
 * @date 2021/11/1 11:38
 */
@Configuration
public class RocketMessageQueueConfig {
    @Resource
    private RocketProperties rocketProperties;

    @Bean(name = "cdcProducer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean cdcProducer(){
        ProducerBean bean = new ProducerBean();
        Properties properties = init();
        properties.setProperty(PropertyKeyConst.GROUP_ID, rocketProperties.getMeta().getCdc().getTopic());
        bean.setProperties(properties);
        return bean;
    }

    private Properties init() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, rocketProperties.getNameServerAddr());
        properties.setProperty(PropertyKeyConst.AccessKey, rocketProperties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, rocketProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, rocketProperties.getSendMsgTimeoutMillis());
        return properties;
    }

}
