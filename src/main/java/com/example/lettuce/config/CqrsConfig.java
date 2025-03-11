package com.example.lettuce.config;

import org.springframework.context.annotation.Configuration;

import com.example.lettuce.global.framework.cqrs.Command;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.framework.cqrs.Query;
import com.example.lettuce.global.framework.cqrs.QueryHandler;
import com.example.lettuce.global.framework.cqrs.CommandBus;
import com.example.lettuce.global.framework.cqrs.QueryBus;

import java.util.List;

import org.springframework.core.ResolvableType;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CqrsConfig {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final List<CommandHandler<? extends Command<?>, ?>> commandHandlers;
    private final List<QueryHandler<? extends Query<?>, ?>> queryHandlers;

    @PostConstruct
    public void registerHandlers() {
        registerCommandHandlers();
        registerQueryHandlers();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerCommandHandlers() {
        commandHandlers.forEach(handler -> {
            Class<?> commandType = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class)
                    .getGeneric(0)
                    .getRawClass();

            commandBus.register((Class) commandType, (CommandHandler) handler);
        });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerQueryHandlers() {
        queryHandlers.forEach(handler -> {
            Class<?> queryType = ResolvableType.forClass(handler.getClass())
                    .as(QueryHandler.class)
                    .getGeneric(0)
                    .getRawClass();

            queryBus.register((Class) queryType, (QueryHandler) handler);
        });
    }
}