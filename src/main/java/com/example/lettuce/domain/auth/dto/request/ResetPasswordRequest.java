package com.example.lettuce.domain.auth.dto.request;

import com.example.lettuce.global.framework.security.validator.ValidPassword;

public record ResetPasswordRequest(@ValidPassword String password) {
}
