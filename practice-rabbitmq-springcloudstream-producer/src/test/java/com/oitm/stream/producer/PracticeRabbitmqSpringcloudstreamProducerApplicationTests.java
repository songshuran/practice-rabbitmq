package com.oitm.stream.producer;

import com.oitm.stream.producer.stream.RabbitmqSender;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PracticeRabbitmqSpringcloudstreamProducerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private RabbitmqSender rabbitmqSender;


	@Test
	public void sendMessageTest1() {
		try {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("SERIAL_NUMBER", "12345");
			properties.put("BANK_NUMBER", "abc");
			properties.put("PLAT_SEND_TIME", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
			rabbitmqSender.sendMessage("Hello World, RMQ Is So Cool", properties);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
