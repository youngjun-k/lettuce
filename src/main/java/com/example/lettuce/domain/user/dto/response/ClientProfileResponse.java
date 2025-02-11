package com.example.lettuce.domain.user.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.example.lettuce.domain.user.dto.response.ProfileResponse.ProfileSpecificResponse;
import com.example.lettuce.domain.user.enums.FitnessGoal;

public record ClientProfileResponse(
        LocalDate birthday,
        String gender,
        Float height,
        Float weight,
        Set<FitnessGoal> fitnessGoals,
        Integer activityLevel) implements ProfileSpecificResponse {
}
