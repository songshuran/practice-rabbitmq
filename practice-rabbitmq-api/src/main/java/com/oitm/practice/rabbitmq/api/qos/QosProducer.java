package com.oitm.practice.rabbitmq.api.qos;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 限流模式生产者
 * @Author: song_shu_ran
 * @Date: 2019-02-24 14:20
 */

public class QosProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();
        channel.basicPublish(Constants.QOS_EXCHANGE_NAME, Constants.QOS_ROUTING_KEY, true, null, Constants.MESSAGE.getBytes());
        //        ChannelFactory.closeResource();
    }
}
