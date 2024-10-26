package com.record.model;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class CustomizeDelayQueue implements Delayed {

    public Long time;

    public Long id;


    public CustomizeDelayQueue(Long time, Long id, TimeUnit unit) {
        this.time = System.currentTimeMillis() + (time > 0 ? unit.toMillis(time) : 0);
        this.id = id;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        CustomizeDelayQueue delay = (CustomizeDelayQueue) o;
        long diff = this.time - delay.time;
        return diff <= 0 ? -1 : 1;
    }
}
