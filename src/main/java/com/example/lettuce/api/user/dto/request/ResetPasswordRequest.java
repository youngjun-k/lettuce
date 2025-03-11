package com.example.lettuce.api.user.dto.request;

import com.example.lettuce.global.framework.security.annotation.ValidPassword;

public record ResetPasswordRequest(@ValidPassword String newPassword) {
}
