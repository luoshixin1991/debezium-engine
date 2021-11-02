package com.boxuegu.debezium.executor;

import io.debezium.engine.DebeziumEngine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.Assert;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * debezium执行器
 *
 * @author lsx
 * @date 2021/10/29 15:26
 */
@Slf4j
public class DebeziumEngineExecutor implements InitializingBean, SmartLifecycle {
    private boolean isRunning = false;
    private final DebeziumEngine<?> debeziumEngine;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public DebeziumEngineExecutor(DebeziumEngine<?> debeziumEngine) {
        this.debeziumEngine = debeziumEngine;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(debeziumEngine, "DebeZiumEngine不能为空!");
    }

    @Override
    public void start() {
        log.info("DebeZiumEngine启动......");
        isRunning = true;
        executor.execute(debeziumEngine);
    }

    @SneakyThrows
    @Override
    public void stop() {
        log.info("DebeZiumEngine关闭......");
        isRunning = false;
        debeziumEngine.close();
    }

    /**
     * 只有该方法返回false时，start方法才会被执行,
     * 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
