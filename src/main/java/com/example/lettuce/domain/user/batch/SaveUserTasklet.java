package com.example.lettuce.domain.user.batch;

import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.domain.carbonfootprint.dao.CarbonFootPrintReward;
import com.example.lettuce.domain.user.dao.User;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveUserTasklet implements Tasklet {

    private final int SIZE = 10;
    private final UserRepository userRepository;

    public SaveUserTasklet(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<User> users = createUsers();

        Collections.shuffle(users);

        userRepository.saveAll(users);

        return RepeatStatus.FINISHED;
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();

        // normal
        for (int i = 0; i < SIZE; i++) {
            users.add(User.builder()                    
                    .rewards(Collections.singletonList(CarbonFootPrintReward.builder()

                            .itemName("item" + i)
                            .awardedPoint(1_000)
                            .build()))
                    .email("username" + i + "@example.com")
                    .password("password" + i)
                    .enabled(true)
                    .verified(true)                    
                    .build());
        }

        // silver
        for (int i = 0; i < SIZE; i++) {
            users.add(User.builder()
                    .rewards(Collections.singletonList(CarbonFootPrintReward.builder()

                            .itemName("item" + i)
                            .awardedPoint(200_000)
                            .build()))
                    .email("username" + i + "@example.com")
                    .password("password" + i)
                    .enabled(true)
                    .verified(true)
                    .build());
        }

        // gold
        for (int i = 0; i < SIZE; i++) {
            users.add(User.builder()
                    .rewards(Collections.singletonList(CarbonFootPrintReward.builder()

                            .itemName("item" + i)
                            .awardedPoint(300_000)
                            .build()))
                    .email("username" + i + "@example.com")
                    .password("password" + i)
                    .enabled(true)
                    .verified(true)
                    .build());
        }

        // vip
        for (int i = 0; i < SIZE; i++) {
            users.add(User.builder()
                    .rewards(Collections.singletonList(CarbonFootPrintReward.builder()

                            .itemName("item" + i)
                            .awardedPoint(500_000)
                            .build()))
                    .email("username" + i + "@example.com")
                    .password("password" + i)
                    .enabled(true)
                    .verified(true)
                    .build());
        }
        return users;
    }
}
