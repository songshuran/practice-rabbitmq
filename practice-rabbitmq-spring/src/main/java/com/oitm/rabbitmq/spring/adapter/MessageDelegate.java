package com.oitm.rabbitmq.spring.adapter;

import com.oitm.rabbitmq.spring.entity.Order;
import com.oitm.rabbitmq.spring.entity.Packaged;

import java.io.File;
import java.util.Map;


public class MessageDelegate {

    //默认规定的方法名字 handleMessage  默认不指定会走这个方法
    public void handleMessage(byte[] messageBody) {
        System.err.println("默认方法, 消息内容:" + new String(messageBody));
    }

    //新版本 如果没有提供 string入参的方法，会自动走 下面的方法，否则走consumeMessage(String messageBody)
    public void consumeMessage(byte[] messageBody) {
        System.err.println("自定义处理message的方法，消息内容:" + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.err.println("自定义 字符串方法, 消息内容:" + messageBody);
    }

    public void method1(String messageBody) {
        System.err.println("method1 收到消息内容:" + new String(messageBody));
    }

    public void method2(String messageBody) {
        System.err.println("method2 收到消息内容:" + new String(messageBody));
    }


    public void consumeMessage(Map messageBody) {
        System.err.println("map方法, 消息内容:" + messageBody);
    }


    public void consumeMessage(Order order) {
        System.err.println("order对象, 消息内容, id: " + order.getId() +
                ", name: " + order.getName() +
                ", content: " + order.getContent());
    }

    public void consumeMessage(Packaged pack) {
        System.err.println("package对象, 消息内容, id: " + pack.getId() +
                ", name: " + pack.getName() +
                ", content: " + pack.getDescription());
    }

    public void consumeMessage(File file) {
        System.err.println("文件对象 方法, 消息内容:" + file.getName());
    }
}
