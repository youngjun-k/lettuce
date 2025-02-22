package com.example.lettuce.domain.user.dao;

import java.time.LocalDate;
import java.time.Period;

import com.example.lettuce.domain.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.domain.user.service.ProfileUpdatable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.example.lettuce.domain.user.enums.Level;


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

    @Override
    public void updateProfile(UpdateClientProfileRequest profileRequest) {
        super.updateBaseProfile(profileRequest);
        this.birthday = profileRequest.getBirthday();
        this.gender = profileRequest.getGender();
    }

    public int getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
}
