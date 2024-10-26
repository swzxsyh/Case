package com.record.component;

import com.record.model.CustomizeDelayQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@Component
public class DelayQueueComponent {

    DelayQueue<CustomizeDelayQueue> delayQueue = new DelayQueue<>();

    public void put(Long time, Long id, TimeUnit unit) {
        delayQueue.put(new CustomizeDelayQueue(time, id, unit));
    }

    public CustomizeDelayQueue poll() {
        return delayQueue.poll();
    }

    public int size() {
        return delayQueue.size();
    }

    public void offer(CustomizeDelayQueue obj) {
        delayQueue.offer(obj);
    }
}
