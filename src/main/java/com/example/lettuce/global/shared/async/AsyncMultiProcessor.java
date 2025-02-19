package com.example.lettuce.global.shared.async;

import com.zaxxer.hikari.HikariDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

@Slf4j
@Component
@Primary
public class AsyncMultiProcessor<E> implements AsyncEventProducer<E> {

    private final List<ReentrantEventQueue<E>> queues = new ArrayList<>();
    private final List<ExecutorService> flattenerExecutors = new ArrayList<>();
    private final List<ExecutorService> leaderExecutors = new ArrayList<>();
    private Consumer<List<E>> saveFunction;
    private final ObjectProvider<ReentrantEventQueue<E>> objectProvider;

    private static final int DEFAULT_QUEUE_COUNT = 10;
    private static final long DEFAULT_TIMEOUT = 1000;
    private static final int DEFAULT_BULK_SIZE = 100;
    private static final int POOL_SIZE_RATIO = 10;

    public AsyncMultiProcessor(@Value("${queue.count:" + DEFAULT_QUEUE_COUNT + "}") int queueCount,
            @Value("${jdbc.async.timeout:" + DEFAULT_TIMEOUT + "}") Long timeout,
            @Value("${jdbc.async.bulk-size:" + DEFAULT_BULK_SIZE + "}") Integer bulkSize,
            JdbcTemplate jdbcTemplate,
            ObjectProvider<ReentrantEventQueue<E>> objectProvider) {
        this.objectProvider = objectProvider;
        setup(queueCount, timeout, bulkSize, calculatePoolSize(jdbcTemplate));
    }

    public void init(Consumer<List<E>> saveFunction) {
        this.saveFunction = saveFunction;
    }

    @Override
    public void produce(List<E> data) {
        if (data.isEmpty())
            return;
        int selectedQueue = ThreadLocalRandom.current().nextInt(queues.size());
        flattenerExecutors.get(selectedQueue).execute(() -> queues.get(selectedQueue).produce(data));
    }

    private void setup(int queueCount, Long timeout, Integer bulkSize, int poolSize) {
        IntStream.range(0, queueCount).forEach(i -> {
            ReentrantEventQueue<E> queue = objectProvider.getObject(timeout, bulkSize);
            queues.add(queue);

            ExecutorService leaderExecutor = Executors.newFixedThreadPool(poolSize);
            leaderExecutors.add(leaderExecutor);
            flattenerExecutors.add(Executors.newCachedThreadPool());

            startQueueProcessor(queue, leaderExecutor);
        });
    }

    private void startQueueProcessor(ReentrantEventQueue<E> queue, ExecutorService executor) {
        CompletableFuture.runAsync(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    List<E> elements = queue.consume();
                    executor.execute(() -> saveFunction.accept(elements));
                } catch (Exception e) {
                    log.error("Error processing queue: ", e);
                }
            }
        });
    }

    private static int calculatePoolSize(JdbcTemplate jdbcTemplate) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (!(dataSource instanceof HikariDataSource hikariDataSource)) {
            throw new IllegalArgumentException("DataSource must be HikariDataSource");
        }
        int maxPoolSize = hikariDataSource.getMaximumPoolSize();
        int calculatedSize = maxPoolSize * POOL_SIZE_RATIO / 10;
        log.debug("Creating AsyncMultiProcessor with pool size: {}", calculatedSize);
        return calculatedSize;
    }
}