package com.oitm.practice.rabbitmq.api.exchange.topic;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.oitm.practice.rabbitmq.api.common.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: topic类型的消费者
 * @Author: song_shu_ran
 * @Date: 2019-02-21 18:08
 */
public class ExchangeTypeTopicConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();
        String queueName = "topic_queue";

        // #匹配多个单词 routing.xx.xx
        // *只能匹配一个单词 routing.xx
        String routingKey = "routing.#";
        // String routingKey = "routing.*";
        // 交换机声明，具体参数含义查看文档基本都能看的明白
        channel.exchangeDeclare(Constants.TOPIC_EXCHANGE_NAME, ExchangeType.TOPIC.getType(),
                true, false, false, null);

        // 队列声明
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定关系
        channel.queueBind(queueName, Constants.TOPIC_EXCHANGE_NAME, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Received ' " + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
