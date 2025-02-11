package com.example.lettuce.global.shared.serializer;

import com.example.lettuce.domain.user.enums.FitnessGoal;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class FitnessGoalDeserializer extends JsonDeserializer<FitnessGoal> {

    @Override
    public FitnessGoal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase(); // Ensure case insensitivity
        try {
            return FitnessGoal.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorCode.INVALID_FITNESS_GOAL);
        }
    }
}
