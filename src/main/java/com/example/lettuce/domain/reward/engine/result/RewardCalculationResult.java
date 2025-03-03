package com.example.lettuce.domain.reward.engine.result;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * Result object used in the rule engine for reward calculation.
 * Contains the calculated reward points and other metadata.
 */
@Getter
@Setter
public class RewardCalculationResult {
    private int points;
    private BigDecimal carbonSaved;
    private String rewardSource;
    private String rewardDescription;
    private boolean eligible;
    
    public RewardCalculationResult() {
        this.points = 0;
        this.carbonSaved = BigDecimal.ZERO;
        this.eligible = true;
    }
} 