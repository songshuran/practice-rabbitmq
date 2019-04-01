package com.oitm.practice.rabbitmq.api.confirm;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.oitm.practice.rabbitmq.api.common.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 消费者
 * @Author: song_shu_ran
 * @Date: 2019-02-24 12:10
 */

public class ConfirmConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();

        String queueName = "confirm_queue";
        channel.exchangeDeclare(Constants.CONFIRM_EXCHANGE_NAME, ExchangeType.TOPIC.getType(), true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, Constants.CONFIRM_EXCHANGE_NAME, Constants.CONFIRM_ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Received'" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {

        });
    }
}
