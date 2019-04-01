package com.oitm.practice.rabbitmq.api.Listener;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author: song_shu_ran
 * @Date: 2019-02-24 13:48
 */

public class ProducerListener {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange,
                                     String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("路由不到的消息，会在这里进行返回");
                System.out.println("replyText：" + replyText);
                System.out.println("body: " + body);
            }
        });
        // mandatory 参数如果为false，不可达的消息会自动删除
//        channel.basicPublish(Constants.RETURN_EXCHANGE_NAME, Constants.RETURN_ROUTING_KEY, true,
//                null, Constants.MESSAGE.getBytes());

        channel.basicPublish(Constants.RETURN_EXCHANGE_NAME, "error.routing", true,
                null, Constants.MESSAGE.getBytes());
    }
}
