package com.example.lettuce.domain.reward.engine.fact;

import java.math.BigDecimal;

import com.example.lettuce.domain.user.aggregate.enums.UserTier;

import lombok.Builder;
import lombok.Getter;

/**
 * Fact object used in the rule engine for reward calculation.
 * Contains all the data needed for rule evaluation.
 */
@Getter
@Builder
public class RewardCalculationFact {
    private Long userId;
    private UserTier userTier;
    private BigDecimal carbonValue;
    private BigDecimal carbonReduction;
    private String productCategory;
} 