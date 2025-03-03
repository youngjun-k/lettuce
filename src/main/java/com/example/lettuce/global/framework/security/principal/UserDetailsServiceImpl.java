package com.example.lettuce.global.framework.security.principal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.query.UserQueryService;
import com.example.lettuce.global.shared.exception.AuthenticationException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserQueryService userQueryService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userQueryService.findByEmail(email);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        validateUser(userDetails);

        return userDetails;
    }

    public UserDetails loadUserWithProfileByEmail(String email) throws UsernameNotFoundException {
        User user = userQueryService.findByEmailWithProfiles(email);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        validateUser(userDetails);

        return userDetails;
    }

    private void validateUser(UserDetailsImpl user) {
        if (user == null) {
            throw new AuthenticationException(ErrorCode.NOT_FOUND_USER);
        }

        validateEnabled(user);
        validateCredentialNonExpired(user);
        validateAccountNonLocked(user);
        validateAccountExpired(user);
    }

    private static void validateEnabled(UserDetailsImpl user) {
        if (!user.isEnabled()) {
            throw new AuthenticationException(ErrorCode.USER_DISABLED);
        }
        if (!user.isVerified()) {
            throw new AuthenticationException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
    }

    private static void validateCredentialNonExpired(UserDetailsImpl user) {
        if (!user.isCredentialsNonExpired()) {
            throw new AuthenticationException(ErrorCode.NON_EXPIRED_ACCOUNT);
        }
    }

    private static void validateAccountNonLocked(UserDetailsImpl user) {
        if (!user.isAccountNonLocked()) {
            throw new AuthenticationException(ErrorCode.ACCOUNT_LOCKED);
        }
    }

    private static void validateAccountExpired(UserDetailsImpl user) {
        if (!user.isAccountNonExpired()) {
            throw new AuthenticationException(ErrorCode.ACCOUNT_EXPIRED);
        }
    }

}
