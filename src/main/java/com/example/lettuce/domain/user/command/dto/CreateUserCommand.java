package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.domain.user.aggregate.enums.UserRole;
import com.example.lettuce.global.framework.cqrs.Command;
import lombok.Getter;

@Getter
public abstract class CreateUserCommand implements Command<String> {

        private String email;

        private String password;

        private String nickname;

        private String profileImage;

        private UserRole role;

        @Override
        public String getAggregateId() {
                return email;
        }

        public CreateUserCommand(String email, String password, String nickname, String profileImage, UserRole role) {
                this.email = email;
                this.password = password;
                this.nickname = nickname;
                this.profileImage = profileImage;
                this.role = role;
        }
}
