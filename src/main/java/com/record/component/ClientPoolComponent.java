package com.record.component;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Slf4j
@Component
public class ClientPoolComponent {

    private ThreadPoolExecutor threadPoolExecutor;
    private final int THREAD_NUM = Runtime.getRuntime().availableProcessors();
    private final int MAX_QUEUE_SIZE = 300;

    @PostConstruct
    public void init() {
        threadPoolExecutor = new ThreadPoolExecutor(
                //                 0,
                //                THREAD_NUM * 10,
                THREAD_NUM * 10,
                THREAD_NUM * 10,
                60L,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<>(MAX_QUEUE_SIZE),
                new CustomizeThreadFactory("Client-pool"),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }

    public <T> Future<T> submit(Callable<T> task, int priority) {
        PriorityTask<T> priorityTask = new PriorityTask<>(task, priority);
        // 注意此处是 execute 而不是 submit
        threadPoolExecutor.execute(priorityTask);
        // 返回正确类型的 Future<T>
        return priorityTask;
    }

    public <T> Future<T> submit(Runnable task, T result, int priority) {
        PriorityTask<T> priorityTask = new PriorityTask<>(task, result, priority);
        // 注意此处是 execute 而不是 submit
        threadPoolExecutor.execute(priorityTask);
        // 返回正确类型的 Future<T>
        return priorityTask;
    }

    public void execute(Runnable command, int priority) {
        threadPoolExecutor.execute(new PriorityTask<>(wrap(command), null, priority)); // 注意这里的泛型为null
    }

    private Runnable wrap(Runnable command) {
        return () -> {
            try {
                command.run();
            } catch (Exception e) {
                log.error("Exception in task execution: ", e); // 记录异常日志
                throw e; // 如果需要，可以重新抛出异常
            }
        };
    }
}