package com.example.lettuce.api.user.dto.request;


import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.DeleteAccountCommand;
import jakarta.validation.constraints.NotBlank;

public record DeleteAccountRequest(
        @NotBlank(message = "삭제 이유를 입력해주세요.") String reason) {

    public DeleteAccountCommand toCommand(User user) {
        return new DeleteAccountCommand(user, reason);
    }
}
