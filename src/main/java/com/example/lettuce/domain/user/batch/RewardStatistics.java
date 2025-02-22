package com.example.lettuce.domain.user.batch;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RewardStatistics {
    private String amount;
    private LocalDate date;

    @Builder
    private RewardStatistics(String amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }
}
