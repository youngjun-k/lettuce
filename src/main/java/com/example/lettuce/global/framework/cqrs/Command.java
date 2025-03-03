package com.example.lettuce.global.framework.cqrs;

public interface Command<T> {
    T getAggregateId();
}
