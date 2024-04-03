package com.yangyag.oauth;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

import java.io.IOException;

public class RedisSubscriber implements Client {
    @Override
    public void listenMessage() {
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
        StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub();
        connection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                System.out.println("Message received: " + message);
            }

            @Override
            public void message(String pattern, String channel, String message) {
            }

            @Override
            public void subscribed(String channel, long count) {
            }

            @Override
            public void psubscribed(String pattern, long count) {
            }

            @Override
            public void unsubscribed(String channel, long count) {
            }

            @Override
            public void punsubscribed(String pattern, long count) {
            }
        });

        connection.sync().subscribe("testTopic");

        // 어플리케이션을 종료하기 전까지 대기
        try {
            System.out.println("Press any key to exit...");
            System.in.read();
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        } finally {
            connection.close();
            redisClient.shutdown();
        }
    }
}
