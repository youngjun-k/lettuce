package com.example.lettuce.global.shared.async;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Scope("prototype")
@Component
@Slf4j
public class ReentrantEventQueue<T> implements AsyncEventProducer<T>, AsyncEventConsumer<T> {

    private final LinkedList<T> queue = new LinkedList<>();
    private final long timeout;
    private final int bulkSize;
    private final ReentrantLock bulkLock = new ReentrantLock();
    private final Condition bulkCondition = bulkLock.newCondition();

    public ReentrantEventQueue(@Value("${jdbc.async.timeout}") Long timeout,
            @Value("${jdbc.async.bulk-size}") Integer bulkSize) {
        this.timeout = timeout;
        this.bulkSize = bulkSize;        
    }

    @Override
    public List<T> consume() {
        List<T> result = new ArrayList<>();

        try {
            bulkLock.lockInterruptibly();
            // Case1: Full Flush
            if (queue.size() >= bulkSize) {
                for (int i = 0; i < bulkSize; i++) {
                    result.add(queue.poll());
                }
                return result;
            }
            // Else Case: Blocking
            // Blocked while Queue is Not Empty
            do {
                bulkCondition.await(timeout, TimeUnit.MILLISECONDS);
            } while (queue.isEmpty());

            // Drain remaining elements up to bulkSize
            for (int i = 0; i < bulkSize; i++) {
                result.add(queue.poll());
                if (queue.isEmpty()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bulkLock.unlock();
        }
        return result;
    }

    @Override
    public void produce(List<T> data) {
        bulkLock.lock();
        try {
            queue.addAll(data);
            if (queue.size() >= bulkSize) {
                bulkCondition.signal();
            }
        } finally {
            bulkLock.unlock();
        }
    }
}