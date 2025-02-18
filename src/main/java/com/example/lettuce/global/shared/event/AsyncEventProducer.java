package com.example.lettuce.global.shared.event;

import java.util.List;

public interface AsyncEventProducer<T> {
    void produce(List<T> data);
}
