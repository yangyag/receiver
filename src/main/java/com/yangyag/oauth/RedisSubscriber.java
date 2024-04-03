package com.yangyag.oauth;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

public class RedisSubscriber implements Client {
    @Override
    public void listenMessage() {
        // Redis 클라이언트 생성
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
        // Redis Publish-Subscribe 연결 생성
        StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub();
        connection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                // 메시지 수신 시 출력
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

        // 'testTopic' 구독 시작
        connection.sync().subscribe("testTopic");

        // 어플리케이션을 종료하기 전까지 대기하는 대신에, while 루프로 계속 실행
        while (true) {
            try {
                // 메시지 수신을 위해 짧게 대기
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // 인터럽트 발생 시 while 루프 종료
            }
        }

        // 연결 종료 및 Redis 클라이언트 종료
        connection.close();
        redisClient.shutdown();
    }
}
