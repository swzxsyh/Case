package com.record.platform.diffusion.constant;

import com.pushtechnology.diffusion.client.topics.TopicSelector;

public interface TopicConstant {

    TopicSelector SUBSCRIBE_TOPIC = new TopicSelector() {
        @Override
        public Type getType() {
            return Type.FULL_PATH_PATTERN;
        }

        @Override
        public String getExpression() {
            return "SUBSCRIBE_TOPIC";
        }

        @Override
        public String getPathPrefix() {
            return "SUBSCRIBE_TOPIC";
        }

        @Override
        public boolean selects(String s) {
            return false;
        }
    };
}
