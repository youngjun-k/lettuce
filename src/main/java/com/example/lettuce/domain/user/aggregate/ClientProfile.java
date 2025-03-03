package com.example.lettuce.domain.user.aggregate;

import java.time.LocalDate;
import java.time.Period;

import com.example.lettuce.domain.user.command.dto.UpdateClientProfileCommand;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "client_profiles")
public class ClientProfile extends Profile implements ProfileUpdatable<UpdateClientProfileCommand> {

    @Column(name = "birthday", nullable = false, columnDefinition = "DATE COMMENT '회원 생년월일'")
    private LocalDate birthday;

    @Column(name = "gender", length = 10, nullable = false, columnDefinition = "VARCHAR(10) COMMENT '회원 성별'")
    private String gender;

    @Override
    public void updateProfile(UpdateClientProfileCommand profileCommand) {
        super.updateBaseProfile(profileCommand);
        this.birthday = profileCommand.getBirthday();
        this.gender = profileCommand.getGender();
    }

    public int getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
}
