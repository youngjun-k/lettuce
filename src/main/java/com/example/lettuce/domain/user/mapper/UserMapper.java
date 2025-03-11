package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.lettuce.api.user.dto.UserInfo;
import com.example.lettuce.domain.user.aggregate.Password;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.aggregate.enums.UserRole;
import com.example.lettuce.domain.user.command.dto.CreateUserCommand;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "role", source = "role")
    @Mapping(target = "userTier", ignore = true)
    @Mapping(target = "rewards", ignore = true)
    @Mapping(target = "clientProfile", ignore = true)
    @Mapping(target = "farmerProfile", ignore = true)
    @Mapping(target = "partnerProfile", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "verified", constant = "false")
    User toUser(CreateUserCommand request, UserRole role);

    @Mapping(target = "userId", source = "id")
    UserInfo toUserInfo(User user);

    default Password map(String password) {
        return new Password(password);
    }
}
