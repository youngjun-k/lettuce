package com.example.lettuce.domain.user.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import com.example.lettuce.domain.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.domain.user.service.ProfileUpdatable;
import com.example.lettuce.domain.user.enums.FitnessGoal;
import com.example.lettuce.global.shared.converter.FitnessGoalConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "client_profiles")
public class ClientProfile extends Profile implements ProfileUpdatable<UpdateClientProfileRequest> {

    @Column(name = "birthday", nullable = false, columnDefinition = "DATE COMMENT '회원 생년월일'")
    private LocalDate birthday;

    @Column(name = "gender", length = 10, nullable = false, columnDefinition = "VARCHAR(10) COMMENT '회원 성별'")
    private String gender;

    @Column(name = "height", nullable = false, columnDefinition = "FLOAT COMMENT '회원 키'")
    private Float height;

    @Column(name = "weight", nullable = false, columnDefinition = "FLOAT COMMENT '회원 몸무게'")
    private Float weight;

    @Builder.Default
    @Convert(converter = FitnessGoalConverter.class)
    @Column(name = "fitness_goals", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '회원 운동 목표'")
    private Set<FitnessGoal> fitnessGoals = new HashSet<>();

    @Column(name = "activity_level", nullable = false, columnDefinition = "INT COMMENT '회원 활동 레벨'")
    private Integer activityLevel;

    @Override
    public void updateProfile(UpdateClientProfileRequest profileRequest) {
        super.updateBaseProfile(profileRequest);
        this.birthday = profileRequest.getBirthday();
        this.gender = profileRequest.getGender();
        this.height = profileRequest.getHeight();
        this.weight = profileRequest.getWeight();
        this.fitnessGoals = profileRequest.getFitnessGoals();
        this.activityLevel = profileRequest.getActivityLevel();
    }

    public int getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
}
