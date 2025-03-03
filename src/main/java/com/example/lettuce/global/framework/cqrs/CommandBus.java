package com.example.lettuce.global.framework.cqrs;

public interface CommandBus {
    <T extends Command<?>, R> R dispatch(T command);

    <T extends Command<?>, R> void register(Class<T> commandType, CommandHandler<T, R> handler);
}
