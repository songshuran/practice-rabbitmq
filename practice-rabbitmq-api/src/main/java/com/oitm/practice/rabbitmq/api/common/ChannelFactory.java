package com.oitm.practice.rabbitmq.api.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 获取channel通用类
 * @Author: song_shu_ran
 * @Date: 2019-02-21 15:33
 */
public class ChannelFactory {

    public static Channel channel;
    public static Connection connection;

    /**
     * @Description: 获取channel对象
     * @Param: []
     * @Return: com.rabbitmq.client.Channel
     */
    public static Channel createChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        return channel;
    }

    /**
     * @Description: 使用完毕后释放资源
     * @Param: []
     * @Return: void
     */
    public static void closeResource() throws IOException, TimeoutException {
        if (channel != null) {
            channel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

}
