package com.oitm.practice.rabbitmq.api.exchange.topic;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: topic类型的生产者
 * @Author: song_shu_ran
 * @Date: 2019-02-21 18:08
 */
public class ExchangeTypeTopicProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();
        String routingKey1 = "routing.key1";
        String routingKey2 = "routing.key2";
        String routingKey3 = "routing.key2.key";
        channel.basicPublish(Constants.TOPIC_EXCHANGE_NAME, routingKey1, null, Constants.MESSAGE.getBytes());
        channel.basicPublish(Constants.TOPIC_EXCHANGE_NAME, routingKey2, null, Constants.MESSAGE.getBytes());
        channel.basicPublish(Constants.TOPIC_EXCHANGE_NAME, routingKey3, null, Constants.MESSAGE.getBytes());
        ChannelFactory.closeResource();
    }
}
