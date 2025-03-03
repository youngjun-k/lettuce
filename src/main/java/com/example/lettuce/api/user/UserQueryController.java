package com.example.lettuce.api.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lettuce.api.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.query.UserQueryService;
import com.example.lettuce.domain.user.query.UserProfileQuery;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.global.shared.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService queryService;

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<ProfileResponse>> getProfile(@LoginUser User user) {
        return CommonResponse.success(SuccessCode.SUCCESS, queryService.getProfile(new UserProfileQuery(user)));
    }
}
