package com.oitm.practice.rabbitmq.api.Listener;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.oitm.practice.rabbitmq.api.common.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author: song_shu_ran
 * @Date: 2019-02-24 13:48
 */

public class ConsumerListener {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();

        String queueName = "return_queue";
        channel.exchangeDeclare(Constants.RETURN_EXCHANGE_NAME, ExchangeType.TOPIC.getType(), true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, Constants.RETURN_EXCHANGE_NAME, Constants.RETURN_ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" Received'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            System.out.printf("consumerTag : " + consumerTag);
        });

    }
}
