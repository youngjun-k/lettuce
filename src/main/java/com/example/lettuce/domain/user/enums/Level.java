package com.example.lettuce.domain.user.enums;

import java.util.Objects;

public enum Level {
    VIP(500_000, null),
    GOLD(500_000, VIP),
    SILVER(300_000, GOLD),
    NORMAL(200_000, SILVER);

    private final int requiredCarbonFootprint;
    private final Level nextLevel;

    Level(int requiredCarbonFootprint, Level nextLevel) {
        this.requiredCarbonFootprint = requiredCarbonFootprint;
        this.nextLevel = nextLevel;
    }

    public int getRequiredCarbonFootprint() {
        return requiredCarbonFootprint;
    }

    public static boolean availableLevelUp(Level level, int totalAmount) {
        if (Objects.isNull(level)) {
            return false;
        }

        if (Objects.isNull(level.nextLevel)) {
            return false;
        }

        return totalAmount >= level.nextLevel.requiredCarbonFootprint;
    }

    public static Level getNextLevel(int totalAmount) {
        // return Arrays.stream(values())
        // .filter(x -> totalAmount >= x.nextAmount)
        // .findFirst()
        // .map(x -> x.nextLevel)
        // .orElse(Level.VIP);
        if (totalAmount >= Level.VIP.requiredCarbonFootprint) {
            return Level.VIP;
        }

        if (totalAmount >= Level.GOLD.requiredCarbonFootprint) {
            return Level.GOLD;
        }

        if (totalAmount >= Level.SILVER.requiredCarbonFootprint) {
            return Level.SILVER;
        }

        if (totalAmount >= Level.NORMAL.requiredCarbonFootprint) {
            return Level.NORMAL;
        }

        return Level.NORMAL;
    }
}
