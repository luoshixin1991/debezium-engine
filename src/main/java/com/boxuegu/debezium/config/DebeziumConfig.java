package com.boxuegu.debezium.config;

import com.boxuegu.debezium.consumer.DbChangeConsumer;
import com.boxuegu.debezium.executor.DebeziumEngineExecutor;
import com.boxuegu.debezium.property.DebeziumProperties;
import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * debezium配置
 *
 * @author lsx
 * @date 2021/10/29 15:02
 */
@Configuration
public class DebeziumConfig {
    @Resource
    private DebeziumProperties debeziumProperties;
    @Resource
    private DbChangeConsumer dbChangeConsumer;

    @Bean
    io.debezium.config.Configuration config() {
        DebeziumProperties.Database database = debeziumProperties.getDatabase();
        DebeziumProperties.ChangeDataCapture cdc = debeziumProperties.getCdc();

        return io.debezium.config.Configuration.create()
                // 连接器的唯一名称
                .with("name", "engine")
                // 偏移量持久化，用来容错 默认值
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                // 偏移量持久化文件路径 默认/tmp/offsets.dat
                .with("offset.storage.file.filename", cdc.getOffsetFile())
                // 捕获偏移量的周期
                .with("offset.flush.interval.ms", "0")
                // 数据库的hostname
                .with("database.hostname", database.getIp())
                .with("database.port", database.getPort())
                .with("database.user", database.getUser())
                .with("database.password", database.getPassword())
                // mysql.cnf 配置的 server-id
                .with("database.server.id", database.getServerId())
                // MySQL 服务器或集群的逻辑名称
                .with("database.server.name", database.getServerName())
                // 历史变更记录
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                // 历史变更记录存储位置
                .with("database.history.file.filename", cdc.getHistoryFile())
                // 连接器的Java类名称
                .with("connector.class", MySqlConnector.class.getName())
                // 包含的数据库列表
                .with("database.include.list", cdc.getDbIncludeList())
                .with("table.include.list", cdc.getTableIncludeList())
////                .with("table.whitelist", "db_test.t_employee")
                // 是否包含数据库表结构层面的变更，建议使用默认值true
                .with("include.schema.changes", "false")
                .with("converter.schemas.enable", "false")
//                .with("signal.data.collection", "db_test.t_employee")
                .build();
    }

    /**
     * 实例化DebeziumEngineExecutor实时同步服务类，执行任务
     */
    @Bean
    DebeziumEngineExecutor debeziumEngineExecutor(io.debezium.config.Configuration configuration){
        DebeziumEngine<ChangeEvent<String, String>> debeziumEngine = DebeziumEngine
                .create(Json.class)
                .using(configuration.asProperties())
                .notifying(dbChangeConsumer)
                .build();

        return new DebeziumEngineExecutor(debeziumEngine);
    }
}
