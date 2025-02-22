package com.example.lettuce.domain.user.enums;

import java.util.Objects;

public enum UserTier {
    VIP(500_000, null),
    GOLD(500_000, VIP),
    SILVER(300_000, GOLD),
    NORMAL(200_000, SILVER);

    private final int requiredCarbonFootprint;
    private final UserTier nextLevel;

    UserTier(int requiredCarbonFootprint, UserTier nextLevel) {
        this.requiredCarbonFootprint = requiredCarbonFootprint;
        this.nextLevel = nextLevel;
    }

    public int getRequiredCarbonFootprint() {
        return requiredCarbonFootprint;
    }

    public static boolean availableLevelUp(UserTier level, int totalAmount) {
        if (Objects.isNull(level)) {
            return false;
        }

        if (Objects.isNull(level.nextLevel)) {
            return false;
        }

        return totalAmount >= level.nextLevel.requiredCarbonFootprint;
    }

    public static UserTier getNextLevel(int totalAmount) {
        // return Arrays.stream(values())
        // .filter(x -> totalAmount >= x.nextAmount)
        // .findFirst()
        // .map(x -> x.nextLevel)
        // .orElse(UserTier.VIP);
        if (totalAmount >= UserTier.VIP.requiredCarbonFootprint) {
            return UserTier.VIP;
        }

        if (totalAmount >= UserTier.GOLD.requiredCarbonFootprint) {
            return UserTier.GOLD;
        }

        if (totalAmount >= UserTier.SILVER.requiredCarbonFootprint) {
            return UserTier.SILVER;
        }

        if (totalAmount >= UserTier.NORMAL.requiredCarbonFootprint) {
            return UserTier.NORMAL;
        }

        return UserTier.NORMAL;
    }
}
