package com.record.component;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 递归泛型约束
 * </p>
 * 这实现Comparable 的自己, 进行约束效果, 确保比较操作仅在相同类型（PriorityTask<T>）的对象之间进行。
 **/
public class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask<T>> {
    private final int priority;

    public PriorityTask(Callable<T> callable, int priority) {
        super(callable);
        this.priority = priority;
    }

    public PriorityTask(Runnable runnable, T result, int priority) {
        super(runnable, result);
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityTask<T> other) {
        // 优先级高的排在前面
        return Integer.compare(other.priority, this.priority);
    }
}