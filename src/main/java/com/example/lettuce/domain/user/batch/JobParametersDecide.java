package com.example.lettuce.domain.user.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class JobParametersDecide implements JobExecutionDecider {

    public static final FlowExecutionStatus CONTINUE = new FlowExecutionStatus("CONTINUE");
    private final String key;

    public JobParametersDecide(String key) {
        this.key = key;
    }

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String value = jobExecution.getJobParameters().getString(key);

        if (value == null || value.isEmpty()) {
            return FlowExecutionStatus.COMPLETED;
        }

        return CONTINUE;
    }
}
