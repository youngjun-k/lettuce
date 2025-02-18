package com.example.lettuce.global.shared.event;
import java.util.List;

public interface AsyncEventConsumer<T> {
    List<T> consume();
}
