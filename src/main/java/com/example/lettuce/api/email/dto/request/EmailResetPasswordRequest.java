package com.example.lettuce.api.email.dto.request;

import jakarta.validation.constraints.Email;

public record EmailResetPasswordRequest(
        @Email String email) {

}
