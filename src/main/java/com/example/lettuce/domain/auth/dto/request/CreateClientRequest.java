package com.example.lettuce.domain.auth.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.lettuce.domain.user.enums.FitnessGoal;
import com.example.lettuce.global.framework.security.annotation.Age;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateClientRequest extends CreateUserRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Age(min = 14, message = "14세 이상만 가입이 가능합니다.")
    private LocalDate birthday;

    @NotBlank(message = "성별을 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{1}$", message = "성별은 한글 한 글자여야 합니다.")
    private String gender;

    @NotNull(message = "키를 입력해주세요.")
    @Positive(message = "키는 0 이상이어야 합니다.")
    private Float height;

    @NotNull(message = "몸무게를 입력해주세요.")
    @Positive(message = "몸무게는 0 이상이어야 합니다.")
    private Float weight;

    @NotNull(message = "운동 목표를 입력해주세요.")
    @Size(min = 1, message = "운동 목표는 최소 1개 이상이어야 합니다.")
    private List<FitnessGoal> fitnessGoals;

    @NotNull(message = "활동 레벨을 입력해주세요.")
    @Min(value = 0, message = "활동 레벨은 0 이상이어야 합니다.")
    @Max(value = 10, message = "활동 레벨은 10 이하여야 합니다.")
    private Integer activityLevel;
}
