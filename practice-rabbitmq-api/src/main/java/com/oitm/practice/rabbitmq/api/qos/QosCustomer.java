package com.oitm.practice.rabbitmq.api.qos;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.oitm.practice.rabbitmq.api.common.ExchangeType;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 限流模式消费者
 * @Author: song_shu_ran
 * @Date: 2019-02-24 14:20
 */

public class QosCustomer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelFactory.createChannel();

        String queueName = "qos_queue";
        channel.exchangeDeclare(Constants.QOS_EXCHANGE_NAME, ExchangeType.TOPIC.getType(), true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, Constants.QOS_EXCHANGE_NAME, Constants.QOS_ROUTING_KEY);

        channel.basicQos(0, 3, false);
        // autoAck 一定要为false
        channel.basicConsume(queueName, false, new CustomConsumer(channel));
    }
}

class CustomConsumer extends DefaultConsumer {

    private Channel channel;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public CustomConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        // 会给broker应答
        channel.basicAck(envelope.getDeliveryTag(), false);

        //根据业务处理结果来 手动回执 确认是否需要重试
        // channel.basicNack();

        System.out.println("发送ack");
    }
}
