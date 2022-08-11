package com.atguigu.guli.service.trade.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;

/**
 * @author Connor
 * @date 2022/8/6
 */
@SpringBootConfiguration
@Slf4j
public class RabbitMQConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(((@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) -> {
            if (!ack) {
                //消息未到达交换机
                log.error("消息未到达交换机: {}", cause);
            }
        }));

        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            //确保消息能够正常到达交换机或者队列
            log.error("消息未到达队列：replyCode = {} , replyText = {} ,exchange = {} ,routingKey = {} ， msg = {}"
                    , replyCode, replyText, exchange, routingKey, new String(message.getBody()));
        });
    }
}
