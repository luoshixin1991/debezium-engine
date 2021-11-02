package com.boxuegu.debezium;

import com.boxuegu.debezium.property.DebeziumProperties;
import com.boxuegu.debezium.property.RocketProperties;
import com.boxuegu.debezium.rocket.producer.BxgCdcProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;


@Slf4j
@SpringBootTest
class ApplicationTests {

    @Resource
    private RocketProperties basicInfoProperties;

    @Resource
    private BxgCdcProducer bxgCdcProducer;
    @Resource
    private DebeziumProperties debeziumProperties;

    @Test
    void contextLoads() throws InterruptedException {
          log.info("数据：{}", debeziumProperties);
    }


}
