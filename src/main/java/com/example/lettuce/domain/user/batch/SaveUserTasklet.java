package com.example.lettuce.domain.user.batch;

import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.domain.carbonfootprint.aggregate.RewardHistory;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.aggregate.enums.UserTier;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SaveUserTasklet implements Tasklet {

    private final int SIZE = 10;
        private final UserRepository userRepository;

    public SaveUserTasklet(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("SaveUserTasklet execute");
        try {
            List<User> users = createUsers();

            Collections.shuffle(users);

            userRepository.saveAll(users);
        } catch (Exception e) {
            log.error("SaveUserTasklet execute error", e);
        }

        return RepeatStatus.FINISHED;
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        Arrays.stream(UserTier.values()).forEach(tier -> {
            IntStream.range(0, SIZE).forEach(i -> {
                int uniqueId = counter.getAndIncrement();
                users.add(User.builder()
                        .rewards(Collections.singletonList(RewardHistory.builder()
                                .itemName("item" + uniqueId)
                                .awardedPoint(tier.getRequiredCarbonFootprint())
                                .build()))
                        .email("username" + uniqueId + "@example.com")                    
                        .enabled(true)
                        .verified(true)
                        .build());
            });
        });
        return users;
    }
}
