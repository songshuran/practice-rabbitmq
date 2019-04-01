package com.oitm.practice.rabbitmq.api.confirm;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 生产者
 * @Author: song_shu_ran
 * @Date: 2019-02-24 12:10
 */

public class ConfirmProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();
        // 制定确认模式
        channel.confirmSelect();

        channel.basicPublish(Constants.CONFIRM_EXCHANGE_NAME, Constants.CONFIRM_ROUTING_KEY, null, Constants.MESSAGE.getBytes());

        // 添加confirm监听
        channel.addConfirmListener(new ConfirmListener() {
            // 成功回调
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("confirm success");
            }

            // 失败回调
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("confirm fail");
            }
        });

    }
}
