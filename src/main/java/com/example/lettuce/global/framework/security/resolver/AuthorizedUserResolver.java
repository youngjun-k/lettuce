package com.example.lettuce.global.framework.security.resolver;

import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.framework.security.principal.UserDetailsImpl;

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
        final UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Objects.isNull(userDetails) ? null : userDetails.user();
    }
}