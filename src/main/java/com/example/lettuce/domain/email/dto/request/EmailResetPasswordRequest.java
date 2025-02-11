package com.example.lettuce.domain.email.dto.request;

import jakarta.validation.constraints.Email;

public record EmailResetPasswordRequest(
        @Email String email) {

}
