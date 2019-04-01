package com.oitm.rabbitmq.spring;

import com.oitm.rabbitmq.spring.adapter.MessageDelegate;
import com.oitm.rabbitmq.spring.converter.ImageMessageConverter;
import com.oitm.rabbitmq.spring.converter.PDFMessageConverter;
import com.oitm.rabbitmq.spring.converter.TextMessageConverter;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: song_shu_ran
 * @Date: 2019-04-01 11:49
 */

@Configuration
@ComponentScan({"com.oitm.rabbitmq.spring.*"})
public class RMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    // 管理控制的组件
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        // RabbitAdmin 底层实现就是从Spring容器中获取Exchange、Binding、RoutingKey、Queue的@Bean声明，
        // 使用RabbitTemplate的execute方法执行对应的声明、修改、删除等操作
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 设置为true  Spring启动加载RabbitAdmin类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true); //队列持久
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true); //队列持久
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true); //队列持久
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true); //队列持久
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true); //队列持久
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }


    // 消息消费者监听的容器
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        // 消费端标签策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });


//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                System.err.println("----------消费者: " + msg);
//            }
//        });


        // 默认走delegate的 handleMessage 方法
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        container.setMessageListener(adapter);

        //走自定义处理方法
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        container.setMessageListener(adapter);


        //走自定义处理方法   设置队列名称和方法名称进行一一匹配
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("queue001","method1");
//        hashMap.put("queue002","method2");
//        adapter.setQueueOrTagToMethodName(hashMap);
//        container.setMessageListener(adapter);


        //json格式的转换器
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        // 支持java对象转换
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        // 这种方式一定要设置该属性   否则会报错 "xxx is not in the trusted packages xxx "
//        javaTypeMapper.setTrustedPackages("com.oitm.rabbitmq.spring.entity");
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);


        // java对象多映射转换
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
//        idClassMapping.put("order", com.oitm.rabbitmq.spring.entity.Order.class);
//        idClassMapping.put("packaged", com.oitm.rabbitmq.spring.entity.Packaged.class);
//        javaTypeMapper.setIdClassMapping(idClassMapping);
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);



        // mutl convert
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);


        adapter.setMessageConverter(convert);
        container.setMessageListener(adapter);


        return container;
    }
}
