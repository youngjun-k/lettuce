package com.example.lettuce.domain.reward.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootprint;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintRepository;
import com.example.lettuce.domain.user.aggregate.User;

import lombok.RequiredArgsConstructor;

/**
 * Specification that checks if a carbon footprint is eligible for rewards.
 */
@Component
@RequiredArgsConstructor
public class EligibleForRewardSpec implements Specification<CarbonFootprint> {

    private final CarbonFootprintRepository carbonFootprintRepository;
    
    // Minimum carbon reduction to be eligible for rewards
    private static final BigDecimal MINIMUM_CARBON_REDUCTION = BigDecimal.valueOf(0.1);
    
    // Maximum number of rewards per day
    private static final int MAX_REWARDS_PER_DAY = 5;
    
    @Override
    public boolean isSatisfiedBy(CarbonFootprint carbonFootprint) {
        // Check if carbon reduction is above minimum threshold
        if (carbonFootprint.getCarbonReduction().compareTo(MINIMUM_CARBON_REDUCTION) < 0) {
            return false;
        }
        
        // Check if user has not exceeded daily reward limit
        User user = carbonFootprint.getUser();
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        long rewardsToday = carbonFootprintRepository.countByUserAndCreatedAtBetween(
                user, startOfDay, endOfDay);
        
        return rewardsToday < MAX_REWARDS_PER_DAY;
    }
} 