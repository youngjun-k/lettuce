# HikariCP Thread Starvation Issue

## Problem

The application experienced HikariCP warnings in the logs:

```
WARN com.zaxxer.hikari.pool.HikariPool: LettuceHikariCP - Thread starvation or clock leap detected (housekeeper delta=59s174ms).
```

This warning indicates that the HikariCP connection pool's housekeeping thread isn't able to run on schedule, which can be caused by:

1. CPU saturation (high CPU usage)
2. Thread starvation (too many threads competing for CPU time)
3. Long garbage collection pauses
4. Resource contention, especially with high-performance components like Disruptor

## Solution Implemented

We made several changes to improve resource utilization and reduce thread starvation:

### 1. Disruptor Configuration Changes

- Reduced the buffer size from 16384 to 4096
- Reduced worker threads from 4 to 2
- Set thread priority lower than default to avoid starving other threads
- Implemented a more CPU-friendly wait strategy (BlockingWaitStrategy)

### 2. HikariCP Configuration Changes

- Increased minimum idle connections to reduce connection acquisition contention
- Set appropriate maximum pool size based on application needs
- Added leak detection threshold to identify potential connection leaks
- Increased housekeeping period to reduce frequency of maintenance operations

### 3. Profile-Based Configuration

- Created a separate configuration profile for Disruptor settings
- Made wait strategy configurable to allow easy switching between strategies

## Monitoring

To verify the solution is effective, monitor:

1. CPU usage - should be lower after changes
2. Thread counts - should be more stable
3. HikariCP warnings - should occur less frequently or not at all
4. Application performance - overall throughput and latency

## Additional Recommendations

If thread starvation issues persist:

1. Consider implementing application-level throttling for high-volume operations
2. Tune JVM garbage collection settings to minimize pause times
3. Increase server resources if necessary
4. Profile the application to identify other potential bottlenecks

## References

- [HikariCP GitHub Issues - Thread Starvation](https://github.com/brettwooldridge/HikariCP/issues/1673)
- [LMAX Disruptor Performance Tuning](https://lmax-exchange.github.io/disruptor/user-guide/index.html#_performance_tuning)
- [Java Thread Priorities and Operating System Scheduling](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#setPriority-int-) 