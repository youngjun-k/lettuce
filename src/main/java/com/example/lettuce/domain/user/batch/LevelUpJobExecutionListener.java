package com.example.lettuce.domain.user.batch;

import com.example.lettuce.domain.user.dao.User;
import com.example.lettuce.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDate;
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

        log.info("User Level Update Batch Job Summary");
        log.info("----------------------------------");
        log.info("Job Execution ID: {}", jobExecution.getId());
        log.info("Job Status: {}", jobExecution.getStatus());
        log.info("Start Time: {}", jobExecution.getStartTime());
        log.info("End Time: {}", jobExecution.getEndTime());
        log.info("Total Processing Time: {} ms", time);
        log.info("Total Records Processed: {}", users.size());
        log.info("Step Execution Summary:");
        jobExecution.getStepExecutions()
                .forEach(step -> log.info("  Step: {}, Status: {}, Read: {}, Write: {}, Skip: {}",
                        step.getStepName(),
                        step.getStatus(),
                        step.getReadCount(),
                        step.getWriteCount(),
                        step.getSkipCount()));
    }
}
