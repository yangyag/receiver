package com.yangyag.oauth;

import java.util.HashMap;
import java.util.Map;

public class MessageSubscriber {

    private static final Map<String, Client> strategyMap = new HashMap<>();

    static {
        strategyMap.put("redis", new RedisSubscriber());
        strategyMap.put("kafka", new KafkaMessageConsumer());
    }

    public static void main(String[] args) {
        String strategyName = args[0].toLowerCase();
        Client client = strategyMap.get(strategyName);

        client.listenMessage();
    }
}