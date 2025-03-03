package com.example.lettuce.domain.reward.specification;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintRepository;
import com.example.lettuce.domain.user.aggregate.User;

import lombok.RequiredArgsConstructor;

/**
 * Specification that checks if a user has achieved carbon neutrality.
 * Carbon neutrality is achieved when the user has saved at least 1000 kg of CO2.
 */
@Component
@RequiredArgsConstructor
public class CarbonNeutralSpec implements Specification<User> {

    private final CarbonFootprintRepository carbonFootprintRepository;
    
    // Carbon neutrality threshold in kg of CO2
    private static final double CARBON_NEUTRAL_THRESHOLD = 1000.0;
    
    @Override
    public boolean isSatisfiedBy(User user) {
        Optional<Double> totalReduction = carbonFootprintRepository.calculateTotalCarbonReductionByUserId(user.getId());
        
        return totalReduction.isPresent() && totalReduction.get() >= CARBON_NEUTRAL_THRESHOLD;
    }
} 