package com.example.lettuce.global.framework.security.resolver;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.framework.security.principal.UserDetailsImpl;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class AuthorizedUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class)
                && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(principal) || principal.equals("anonymousUser")) {
            throw new BaseException(ErrorCode.INVALID_AUTHENTICATION);
        }
        final UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        return userDetails.user();
    }
}
