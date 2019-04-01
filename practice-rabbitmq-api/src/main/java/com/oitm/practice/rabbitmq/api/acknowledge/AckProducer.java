package com.oitm.practice.rabbitmq.api.acknowledge;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 生产者
 * @Author: song_shu_ran
 * @Date: 2019-02-24 13:32
 */

public class AckProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();
//        channel
    }
}
