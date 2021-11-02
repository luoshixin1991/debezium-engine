package com.boxuegu.debezium.consumer;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.boxuegu.debezium.rocket.producer.BxgCdcProducer;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 批量消费数据库消息
 *
 * @author lsx
 * @date 2021/10/30 20:40
 */
@Slf4j
@Component
public class DbChangeConsumer implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {
    @Resource
    private BxgCdcProducer bxgCdcProducer;

    @Override
    public void handleBatch(List<ChangeEvent<String, String>> records,
                            DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> recordCommitter)
            throws InterruptedException {
        /*
         这里要考虑这些value为Null数据没有调用recordCommitter.markProcessed(r)会不会产生影响
         */
        // 当数据库执行delete时候会有一个delete类型的事件和一条value为null消息{"id":93710}, value: null}
        records = records.stream()
                .filter(r -> !Objects.isNull(r) && !Objects.isNull(r.value()))
                .collect(Collectors.toList());

        final CountDownLatch latch = new CountDownLatch(records.size());
        for (ChangeEvent<String, String> r : records) {
            // 将单个记录标记为已处理，必须为每个记录调用
            recordCommitter.markProcessed(r);
            String msgBody = r.value();
            com.boxuegu.debezium.rocket.bean.ChangeEvent changeEvent = JSON.parseObject(msgBody,
                    com.boxuegu.debezium.rocket.bean.ChangeEvent.class);
            bxgCdcProducer.send(r.value(), r.key(), changeEvent.getSource().getTable(), latch);
        }
        latch.await();
        // 将批标记为已完成，这可能导致提交偏移/刷新，当一批记录处理完毕时应调用
        recordCommitter.markBatchFinished();
    }
}
