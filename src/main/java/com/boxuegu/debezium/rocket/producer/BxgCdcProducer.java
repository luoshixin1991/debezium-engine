package com.boxuegu.debezium.rocket.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.boxuegu.debezium.property.RocketProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * rocketMQ 通用生产者
 *
 * @author lsx
 * @date 2021/11/1 11:54
 */
@Slf4j
@Component
public class BxgCdcProducer {
    private static final int RETRY = 5;

    @Resource
    private RocketProperties rocketProperties;
    @Resource
    private ProducerBean cdcProducer;

    @Async("cdcProducerTaskExecutor")
    public void send(String body, String key, String tag, CountDownLatch latch) {
        send(body, key, tag);
        latch.countDown();
    }

    private SendResult send(String body, String key, String tag) {
        Message message = new Message(rocketProperties.getMeta().getCdc().getTopic(),
                tag, key, body.getBytes(StandardCharsets.UTF_8));
        message.setBornTimestamp(System.currentTimeMillis());
        SendResult result = null;
        for (int retry = RETRY; retry > 0; retry--) {
            try {
                result = cdcProducer.send(message);
            } catch (Exception ex) {
                log.error("[BxgCdcProducer#send]消息[{},{}]事件:[{}],第[{}]次发送失败", key, tag, body, 11 - retry, ex);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(11 - retry));
                continue;
            }
            break;
        }
        return result;
    }
}
