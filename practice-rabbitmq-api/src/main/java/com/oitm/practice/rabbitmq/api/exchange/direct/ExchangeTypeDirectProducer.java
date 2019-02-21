package com.oitm.practice.rabbitmq.api.exchange.direct;

import com.oitm.practice.rabbitmq.api.common.ChannelFactory;
import com.oitm.practice.rabbitmq.api.common.Constants;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: direct类型的生产者
 * @Author: song_shu_ran
 * @Date: 2019-02-21 15:44
 */
public class ExchangeTypeDirectProducer {

	public static void main(String[] args) throws IOException, TimeoutException {

		Channel channel = ChannelFactory.createChannel();
		channel.basicPublish(Constants.DIRECT_EXCHANGE_NAME,
				Constants.DIRECT_ROUTING_KEY, null, Constants.MESSAGE.getBytes());

		ChannelFactory.closeResource();
	}

}
