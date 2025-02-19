package com.example.lettuce.global.shared.async;

import java.util.List;

public interface AsyncEventConsumer<T> {
    List<T> consume();
}
