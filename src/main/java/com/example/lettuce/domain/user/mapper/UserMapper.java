package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.lettuce.domain.auth.dto.request.CreateUserRequest;
import com.example.lettuce.domain.user.dao.User;
import com.example.lettuce.domain.user.dto.UserInfo;
import com.example.lettuce.domain.user.enums.UserRole;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role")
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "rewards", ignore = true)
    @Mapping(target = "clientProfile", ignore = true)
    @Mapping(target = "farmerProfile", ignore = true)
    @Mapping(target = "partnerProfile", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "verified", constant = "false")
    User toUser(CreateUserRequest request, String encodedPassword, UserRole role);

    @Mapping(target = "userId", source = "id")
    UserInfo toUserInfo(User user);
}
