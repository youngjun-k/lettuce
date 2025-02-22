package com.example.lettuce.domain.user.batch;

import com.example.lettuce.domain.user.dao.User;
import com.example.lettuce.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class LevelUpJobExecutionListener implements JobExecutionListener {

    private final UserRepository userRepository;

    public LevelUpJobExecutionListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        List<User> users = userRepository.findAllByUpdatedAt(LocalDate.now());

        long time = java.time.Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis();

        log.info("회원등급 업데이트 배치 프로그램");
        log.info("-------------------------------");
        log.info("총 데이터 처리 {}건, 처리 시간 : {}millis", users.size(), time);
    }
}
