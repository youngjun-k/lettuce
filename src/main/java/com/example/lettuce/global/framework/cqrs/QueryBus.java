package com.example.lettuce.global.framework.cqrs;

public interface QueryBus {
    <Q extends Query<R>, R> R execute(Q query);

    <Q extends Query<R>, R> void register(Class<Q> queryType, QueryHandler<Q, R> handler);
}