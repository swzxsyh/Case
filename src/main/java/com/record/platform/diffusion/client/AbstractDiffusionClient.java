package com.record.platform.diffusion.client;

import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.TopicSelector;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;
import com.pushtechnology.diffusion.datatype.json.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AbstractDiffusionClient {

    protected boolean isSessionActive(Session session) {
        return session.getState().isConnected();
    }

    protected void doSubscribe(Topics topics, TopicSelector selector) {
        topics.addStream(selector, JSON.class, new Topics.ValueStream.Default<>() {
            @Override
            public void onSubscription(String topicPath, TopicSpecification specification) {
                super.onSubscription(topicPath, specification);
            }

            @Override
            public void onValue(String topicPath, TopicSpecification specification, JSON oldValue, JSON newValue) {
                String str = newValue.toJsonString();
                // do something
                log.info("onValue:{}", str);
            }

            @Override
            public void onClose() {
                super.onClose();
            }

            @Override
            public void onError(ErrorReason errorReason) {
                super.onError(errorReason);
            }

            @Override
            public void onUnsubscription(String topicPath, TopicSpecification specification, Topics.UnsubscribeReason reason) {
                super.onUnsubscription(topicPath, specification, reason);
            }
        });
    }
}
