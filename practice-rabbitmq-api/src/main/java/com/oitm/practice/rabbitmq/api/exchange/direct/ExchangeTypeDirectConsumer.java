package com.oitm.practice.rabbitmq.api.exchange.direct;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.oitm.practice.rabbitmq.api.common.ExchangeType;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: direct类型的消费者
 * @Author: song_shu_ran
 * @Date: 2019-02-21 15:44
 */
public class ExchangeTypeDirectConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();

        String queueName = "direct_queue";
        // 交换机声明，具体参数含义查看文档基本都能看的明白
        channel.exchangeDeclare(Constants.DIRECT_EXCHANGE_NAME, ExchangeType.DIRECT.getType(),
                true, false, false, null);

        // 队列声明
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定关系
        channel.queueBind(queueName, Constants.DIRECT_EXCHANGE_NAME, Constants.DIRECT_ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Received '" + message + "'");
            // 获取properties中的内容
            AMQP.BasicProperties properties = delivery.getProperties();
            System.out.println("headers:" + properties.getHeaders());
            System.out.println("encoding:" + properties.getContentEncoding());
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
