package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;

public interface ProfileStrategy {

    void validateRequest(UpdateUserCommand request);

    void updateProfile(Profile profile, UpdateUserCommand request);
}
