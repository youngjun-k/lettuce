package com.example.lettuce.global.framework.cqrs;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
