package com.record.component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomizeThreadFactory implements ThreadFactory {

    private AtomicInteger threadNumber = new AtomicInteger();

    private String prefix;

    public CustomizeThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        int index = threadNumber.getAndDecrement();
        StringBuilder prefixStringBuilder = new StringBuilder();
        String namePrefix = prefixStringBuilder.append(prefix).append('-').append(index).toString();
        Thread thread = new Thread(r, namePrefix);
        if (thread.isDaemon()) {
            thread.setDaemon(Boolean.FALSE);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }

        return thread;
    }
}
