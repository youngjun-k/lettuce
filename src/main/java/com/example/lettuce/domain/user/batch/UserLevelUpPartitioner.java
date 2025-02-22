package com.example.lettuce.domain.user.batch;

import com.example.lettuce.domain.user.repository.UserRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserLevelUpPartitioner implements Partitioner {

    private final UserRepository userRepository;

    public UserLevelUpPartitioner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0");
        }

        long minId = userRepository.findMinId(); // 1
        long maxId = userRepository.findMaxId(); // 4000

        if (minId > maxId) {
            throw new IllegalArgumentException("Min ID must be less than max ID");
        }

        if (minId == maxId) {
            Map<String, ExecutionContext> singlePartition = new HashMap<>();
            ExecutionContext context = new ExecutionContext();
            context.putLong("minId", minId);
            context.putLong("maxId", maxId);
            singlePartition.put("partition0", context);
            return singlePartition;
        }

        long targetSize = (maxId - minId) / gridSize + 1; // 500

        /**
         * partition0 : 1, 500
         * partition1 : 501, 1000
         * partition2 : 1001, 1500
         * ....
         * partition8 : 3501, 4000
         */
        Map<String, ExecutionContext> result = new HashMap<>();
        long number = 0;
        long start = minId;
        long end = start + targetSize - 1;

        while (start <= maxId) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= maxId) {
                end = maxId;
            }

            value.putLong("minId", start);
            value.putLong("maxId", end);
            start += targetSize;
            end += targetSize;
            number++;

            // Safety check to prevent infinite loop
            if (number > gridSize) {
                log.error("Partition number must be less than grid size");
                throw new IllegalArgumentException("Partition number must be less than grid size");
            }
        }

        return result;
    }
}
