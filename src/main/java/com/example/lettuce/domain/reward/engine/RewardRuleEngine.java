package com.example.lettuce.domain.reward.engine;

import java.math.BigDecimal;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootprint;
import com.example.lettuce.domain.reward.engine.fact.RewardCalculationFact;
import com.example.lettuce.domain.reward.engine.result.RewardCalculationResult;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.aggregate.enums.UserTier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Rule engine for calculating rewards based on carbon footprint data.
 * Uses Drools to apply dynamic reward calculation rules.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewardRuleEngine {

    private final KieContainer kieContainer;
    
    /**
     * Calculates rewards based on carbon footprint data and user information.
     * 
     * @param user The user for whom to calculate rewards
     * @param carbonFootprint The carbon footprint data
     * @return The calculated reward result
     */
    public RewardCalculationResult calculateReward(User user, CarbonFootprint carbonFootprint) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            // Create fact object with all the data needed for rule evaluation
            RewardCalculationFact fact = RewardCalculationFact.builder()
                    .userId(user.getId())
                    .userTier(user.getUserTier())
                    .carbonValue(carbonFootprint.getCarbonValue())
                    .carbonReduction(carbonFootprint.getCarbonReduction())
                    .productCategory(carbonFootprint.getProductCategory())
                    .build();
            
            // Create result object to be populated by rules
            RewardCalculationResult result = new RewardCalculationResult();
            
            // Insert facts and result into the session
            kieSession.insert(fact);
            kieSession.insert(result);
            
            // Fire all rules
            kieSession.fireAllRules();
            
            return result;
        } finally {
            kieSession.dispose();
        }
    }
    
    /**
     * Factory method to create a reward calculator based on user tier.
     * This implements the Factory Method pattern for creating different reward calculators.
     */
    public RewardCalculator createRewardCalculator(UserTier userTier) {
        return switch (userTier) {
            case NORMAL -> new BasicRewardCalculator();
            case SILVER -> new SilverRewardCalculator();
            case GOLD -> new GoldRewardCalculator();
            case VIP -> new VipRewardCalculator();
        };
    }
    
    /**
     * Interface for reward calculators.
     */
    public interface RewardCalculator {
        int calculatePoints(BigDecimal carbonReduction, String productCategory);
    }
    
    /**
     * Basic reward calculator for normal users.
     */
    private class BasicRewardCalculator implements RewardCalculator {
        @Override
        public int calculatePoints(BigDecimal carbonReduction, String productCategory) {
            return carbonReduction.multiply(BigDecimal.valueOf(10)).intValue();
        }
    }
    
    /**
     * Silver reward calculator with 20% bonus.
     */
    private class SilverRewardCalculator implements RewardCalculator {
        @Override
        public int calculatePoints(BigDecimal carbonReduction, String productCategory) {
            return carbonReduction.multiply(BigDecimal.valueOf(12)).intValue();
        }
    }
    
    /**
     * Gold reward calculator with 50% bonus.
     */
    private class GoldRewardCalculator implements RewardCalculator {
        @Override
        public int calculatePoints(BigDecimal carbonReduction, String productCategory) {
            return carbonReduction.multiply(BigDecimal.valueOf(15)).intValue();
        }
    }
    
    /**
     * VIP reward calculator with 100% bonus.
     */
    private class VipRewardCalculator implements RewardCalculator {
        @Override
        public int calculatePoints(BigDecimal carbonReduction, String productCategory) {
            return carbonReduction.multiply(BigDecimal.valueOf(20)).intValue();
        }
    }
}