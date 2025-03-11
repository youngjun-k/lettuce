package com.example.lettuce.global.framework.cqrs;

public interface CommandHandler<T extends Command<?>, R> {
    R handle(T command);
}
