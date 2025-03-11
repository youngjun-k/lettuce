package com.example.lettuce.global.framework.cqrs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SimpleCommandBus implements CommandBus {
    private final Map<Class<? extends Command<?>>, CommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    @Override
    public <T extends Command<?>, R> R dispatch(T command) {
        CommandHandler<T, R> handler = findHandler(command);
        return handler.handle(command);
    }

    @Override
    public <T extends Command<?>, R> void register(Class<T> commandType, CommandHandler<T, R> handler) {
        handlers.put(commandType, handler);
    }

    @SuppressWarnings("unchecked")
    private <T extends Command<?>, R> CommandHandler<T, R> findHandler(T command) {
        CommandHandler<T, R> handler = (CommandHandler<T, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for command: " + command.getClass().getName());
        }
        return handler;
    }
}
