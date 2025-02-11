package com.example.lettuce.domain.user.enums;

import com.example.lettuce.global.shared.serializer.FitnessGoalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = FitnessGoalDeserializer.class)
public enum FitnessGoal {
    LOSE_WEIGHT,
    GAIN_MUSCLE,
    IMPROVE_HEALTH,
    IMPROVE_MENTAL_HEALTH,
    IMPROVE_PHYSICAL_HEALTH,
    IMPROVE_SLEEP,
    OTHER
}
