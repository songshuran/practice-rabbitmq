package com.oitm.rabbitmq.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oitm.rabbitmq.spring.entity.Order;
import com.oitm.rabbitmq.spring.entity.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;

    public static final String DIRECT_EXCHANGE = "test.direct";
    public static final String TOPIC_EXCHANGE = "test.topic";
    public static final String FANOUT_EXCHANGE = "test.fanout";


    public static final String DIRECT_NAME = "test.direct.queue";
    public static final String TOPIC_NAME = "test.topic.queue";
    public static final String FANOUT_NAME = "test.fanout.queue";

    @Test
    public void testAdmin() throws Exception {

        // 如果不存在，会创建三个非持久化的Exchange  服务重启会自动删除
        DirectExchange directExchange = new DirectExchange(DIRECT_EXCHANGE, false, false);
        TopicExchange topicExchange = new TopicExchange(TOPIC_EXCHANGE, false, false);
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE, false, false);
        rabbitAdmin.declareExchange(directExchange);
        rabbitAdmin.declareExchange(topicExchange);
        rabbitAdmin.declareExchange(fanoutExchange);


        // 如果不存在，会创建三个非持久化的Queue  服务重启会自动删除
        Queue directQueue = new Queue(DIRECT_NAME, false);
        Queue topicQueue = new Queue(TOPIC_NAME, false);
        Queue fanoutQueue = new Queue(FANOUT_NAME, false);
        rabbitAdmin.declareQueue(directQueue);
        rabbitAdmin.declareQueue(topicQueue);
        rabbitAdmin.declareQueue(fanoutQueue);


        Binding direct = new Binding(DIRECT_NAME,
                Binding.DestinationType.QUEUE,
                TOPIC_EXCHANGE, "direct", new HashMap<>());
        rabbitAdmin.declareBinding(direct);

        Binding bindingTopic = BindingBuilder.bind(topicQueue).to(topicExchange).with("user.#");
        rabbitAdmin.declareBinding(bindingTopic);

        // 直接创建队列 交换机 建立关联关系  指定路由key
        Binding bindingFanout = BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
        rabbitAdmin.declareBinding(bindingFanout);

        // 清空队列消息
        rabbitAdmin.purgeQueue(TOPIC_NAME, false);
    }

    //消息模板
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("key", "value");

        Message message = new Message("hello world".getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("额外设置");
                message.getMessageProperties().getHeaders().put("key", "value2");
                message.getMessageProperties().getHeaders().put("attr", "新加属性");
                return message;
            }
        });
    }

    @Test
    public void testSendMessage2() throws Exception {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("mq 消息1234".getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.abc", message);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object message send!");
        rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "hello object message send!");
    }

    @Test
    public void testSendMessage4Text() throws Exception {
        //1 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("mq 消息1234".getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.abc", message);
        rabbitTemplate.send("topic002", "rabbit.abc", message);
    }


    @Test
    public void testSendJsonMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //修改contentType为 application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }


    @Test
    public void testSendJavaMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //修改contentType为 application/json
        // 设置headers：__TypeId__
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "com.oitm.rabbitmq.spring.entity.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }


    @Test
    public void testSendMappingMessage() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");

        String json1 = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json1);

        MessageProperties messageProperties1 = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        // 设置headers：__TypeId__
        messageProperties1.setContentType("application/json");
        messageProperties1.getHeaders().put("__TypeId__", "order");
        Message message1 = new Message(json1.getBytes(), messageProperties1);
        rabbitTemplate.send("topic001", "spring.order", message1);


        Packaged pack = new Packaged();
        pack.setId("002");
        pack.setName("包裹消息");
        pack.setDescription("包裹描述信息");

        String json2 = mapper.writeValueAsString(pack);
        System.err.println("pack 4 json: " + json2);

        MessageProperties messageProperties2 = new MessageProperties();
        messageProperties2.setContentType("application/json");
        messageProperties2.getHeaders().put("__TypeId__", "packaged");
        Message message2 = new Message(json2.getBytes(), messageProperties2);
        rabbitTemplate.send("topic001", "spring.pack", message2);
    }


    @Test
    public void testSendMutlConverterMessage() throws Exception {
        // 图片消息
//        byte[] body = Files.readAllBytes(Paths.get("/Users/Oitm/Desktop/Test", "Oitm.jpeg"));
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("image");
//        messageProperties.getHeaders().put("extName", "jpeg");
//        Message message = new Message(body, messageProperties);
//        rabbitTemplate.send("", "image_queue", message);


        //pdf文件消息
        byte[] body = Files.readAllBytes(Paths.get("/Users/Oitm/Desktop/Test", "jumpserver.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(body, messageProperties);
        rabbitTemplate.send("", "pdf_queue", message);
    }


}
