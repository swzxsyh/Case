package com.record.component;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Thread Pool
 *
 * @author swzxsyh
 */
@Component
public class ThreadPoolComponent {

    private ThreadPoolExecutor threadPoolExecutor;

    // runtime
    private final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

    @PostConstruct
    public void init() {
        threadPoolExecutor = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM * 2 + 1, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(THREAD_NUM * 200), new CustomizeThreadFactory("CUSTOMIZE-POOL-"), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public <T> Future<T> submit(Callable<T> task) {
        return threadPoolExecutor.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return threadPoolExecutor.submit(task, result);
    }

    public void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }
}
