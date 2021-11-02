package com.boxuegu.debezium.rocket.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * cdc是事件
 *
 * @author lsx
 * @date 2021/11/1 15:50
 */
@Data
public class ChangeEvent {
    /**
     * cdc事件类型, 取值范围参考io.debezium.data.Envelope.Operation
     */
    private String op;

    @JsonProperty("ts_ms")
    private Long tsMs;

    private Source source;

    @Setter
    @Getter
    @ToString
    public static class Source{
        private String version;
        private String connector;
        private String name;
        @JsonProperty("ts_ms")
        private Long tsMs;
        private String snapshot;
        private String db;
        private String table;
        @JsonProperty("server_id")
        private Integer serverId;
        private String file;
        private Long pos;
        private Long row;
    }
}
