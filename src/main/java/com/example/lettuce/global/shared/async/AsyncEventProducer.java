package com.example.lettuce.global.shared.async;

import java.util.List;

public interface AsyncEventProducer<T> {
    void produce(List<T> data);
}
