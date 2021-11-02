package com.boxuegu.debezium.model.dto;

import lombok.Data;

/**
 * 消息队列配置
 *
 * @author lsx
 * @date 2021/11/1 10:00
 */
@Data
public class RocketMetaDTO {
    private String topic;
    private String producer;
}
