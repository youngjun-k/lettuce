package com.example.lettuce.domain.user.command.dto;

import java.time.LocalDate;

import com.example.lettuce.domain.user.aggregate.enums.UserRole;

import lombok.Getter;

@Getter
public class CreateClientCommand extends CreateUserCommand {

    private LocalDate birthday;

    private String gender;

    public CreateClientCommand(String email, String password, String nickname, String profileImage, UserRole role,
            LocalDate birthday, String gender) {
        super(email, password, nickname, profileImage, role);
        this.birthday = birthday;
        this.gender = gender;
    }
}
