package com.oitm.practice.rabbitmq.api.exchange.fanout;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: fanout类型的生产者
 * @Author: song_shu_ran
 * @Date: 2019-02-21 17:53
 */
public class ExchangeTypeFanoutProducer {

	public static void main(String[] args) throws IOException, TimeoutException {
		Channel channel = ChannelFactory.createChannel();

		channel.basicPublish(Constants.FANOUT_EXCHANGE_NAME, "", null, Constants.MESSAGE.getBytes());

		ChannelFactory.closeResource();
	}

}
