package com.example.lettuce.global.infrastructure.storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * Class representing carbon footprint analytics results.
 */
@Getter
@Builder
public class CarbonFootprintAnalytics {
    private final Long userId;
    private final int totalFootprints;
    private final BigDecimal totalCarbonValue;
    private final BigDecimal totalCarbonReduction;
    private final List<String> categories;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    
    /**
     * Calculates the average carbon reduction per footprint.
     * 
     * @return The average carbon reduction
     */
    public BigDecimal getAverageCarbonReduction() {
        if (totalFootprints == 0) {
            return BigDecimal.ZERO;
        }
        return totalCarbonReduction.divide(BigDecimal.valueOf(totalFootprints), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Calculates the carbon reduction percentage.
     * 
     * @return The carbon reduction percentage
     */
    public BigDecimal getCarbonReductionPercentage() {
        if (totalCarbonValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalCarbonReduction
                .multiply(BigDecimal.valueOf(100))
                .divide(totalCarbonValue, 2, BigDecimal.ROUND_HALF_UP);
    }
} 