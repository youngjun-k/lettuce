package com.example.lettuce.global.shared.converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.lettuce.domain.user.enums.FitnessGoal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FitnessGoalConverter implements AttributeConverter<Set<FitnessGoal>, String> {
    @Override
    public String convertToDatabaseColumn(Set<FitnessGoal> goals) {

        return goals == null ? null
                : String.join(",", goals.stream()
                        .map(FitnessGoal::name)
                        .toList());
    }

    @Override
    public Set<FitnessGoal> convertToEntityAttribute(String dbData) {

        return dbData == null ? null
                : Arrays.stream(dbData.split(","))
                        .map(FitnessGoal::valueOf)
                        .collect(Collectors.toSet());
    }
}