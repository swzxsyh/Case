package com.record.platform.diffusion.service;


import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.session.Session;
import com.record.platform.diffusion.DiffusionSessionManager;
import com.record.platform.diffusion.client.AbstractDiffusionClient;
import com.record.platform.diffusion.constant.TopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SubscribeService extends AbstractDiffusionClient {

    @Autowired
    private DiffusionSessionManager manager;

    private final Object lock = new Object();

    static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new BasicThreadFactory.Builder().namingPattern("Subscribe-reconnect-%d").daemon(true).build());

    public void subscribeToTopics() {
        executor.scheduleAtFixedRate(() -> {
            try {
                synchronized (lock) {
                    Session session = manager.getSession();
                    if (Objects.isNull(session)) {
                        log.error("session create error, session is null");
                        return;
                    }
                    if (!manager.containsKey(session)) {
                        doFeature(session);
                        manager.computeSession(session, TopicConstant.SUBSCRIBE_TOPIC.getExpression());
                    }
                    if (!isSessionActive(session)) {
                        manager.clear(session);
                    }
                }
            } catch (Exception e) {
                log.error("SubscribeService subscribeToTopics error", e);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private synchronized void doFeature(Session session) {
        Topics topics = session.feature(Topics.class);
        // 检查是否已经订阅了该主题
        boolean resubscribe = manager.ensureSession(session, TopicConstant.SUBSCRIBE_TOPIC.getExpression());
        if (resubscribe) {
            log.warn("Diffusion_SUBSCRIBE 当前session重复订阅:{}, session:{}, topic:{}", TopicConstant.SUBSCRIBE_TOPIC.getExpression(), session, topics);
            return;
        }
        // 存储当前会话的 Topics
        manager.computeTopic(session, TopicConstant.SUBSCRIBE_TOPIC.getExpression());

        doSubscribe(topics, TopicConstant.SUBSCRIBE_TOPIC);

        topics.subscribe(TopicConstant.SUBSCRIBE_TOPIC)
                .whenComplete((result, ex) -> {
                    if (Objects.nonNull(ex)) {
                        log.error("Failed to subscribe to topic: {}, e:", TopicConstant.SUBSCRIBE_TOPIC.getExpression(), ex);
                        topics.unsubscribe(TopicConstant.SUBSCRIBE_TOPIC);
                        subscribeToTopics();
                    } else {
                        log.info("Subscribed to topic: {},result:{}", TopicConstant.SUBSCRIBE_TOPIC.getExpression(), result);
                    }
                });
    }
}