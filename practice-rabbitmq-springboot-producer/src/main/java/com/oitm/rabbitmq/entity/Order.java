package com.oitm.rabbitmq.entity;

import java.io.Serializable;

// 注意要实现 Serializable 该接口
public class Order implements Serializable {

    private String id;
    private String name;

    public Order() {
    }

    public Order(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
