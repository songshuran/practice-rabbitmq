package com.oitm.practice.rabbitmq.api.common;

/**
 * @Description: 常量
 * @Author: song_shu_ran
 * @Date: 2019-02-21 16:20
 */
public class Constants {
    public static final String DIRECT_EXCHANGE_NAME = "type_direct_exchange";
    public static final String DIRECT_ROUTING_KEY = "direct.route";

    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_ROUTING_KEY = "confirm.routing";

    public static final String RETURN_EXCHANGE_NAME = "return_exchange";
    public static final String RETURN_ROUTING_KEY = "return.routing";

    public static final String QOS_EXCHANGE_NAME = "qos_exchange";
    public static final String QOS_ROUTING_KEY = "qos.routing";

    public static final String FANOUT_EXCHANGE_NAME = "type_fanout_exchange";

    public static final String TOPIC_EXCHANGE_NAME = "type_topic_exchange";

    public static final String MESSAGE = "Hello World, Just be Happy!";

}
