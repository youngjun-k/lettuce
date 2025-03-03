package com.example.lettuce.global.framework.cqrs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SimpleQueryBus implements QueryBus {
    private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public <Q extends Query<R>, R> R execute(Q query) {
        QueryHandler<Q, R> handler = findHandler(query);
        return handler.handle(query);
    }

    @Override
    public <Q extends Query<R>, R> void register(Class<Q> queryType, QueryHandler<Q, R> handler) {
        handlers.put(queryType, handler);
    }

    @SuppressWarnings("unchecked")
    private <Q extends Query<R>, R> QueryHandler<Q, R> findHandler(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new IllegalStateException(
                    "No handler registered for query: " + query.getClass().getName());
        }
        return handler;
    }
}