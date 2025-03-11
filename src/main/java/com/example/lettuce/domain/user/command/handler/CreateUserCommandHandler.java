package com.example.lettuce.domain.user.command.handler;

import org.springframework.stereotype.Component;

import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.CreateClientCommand;
import com.example.lettuce.domain.user.command.dto.CreateUserCommand;
import com.example.lettuce.domain.user.event.UserRegistrationEvent;
import com.example.lettuce.domain.user.mapper.ProfileMapper;
import com.example.lettuce.domain.user.mapper.ProfileMapperFactory;
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.framework.event.DomainEventPublisher;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CommandHandler<CreateClientCommand, Void> {

    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public Void handle(CreateClientCommand command) {
        validateCommand(command);

        User user = User.create(command);

        @SuppressWarnings("unchecked")
        ProfileMapper<CreateUserCommand, ?, ?> profileMapper = (ProfileMapper<CreateUserCommand, ?, ?>) ProfileMapperFactory
                .getProfileMapper(command.getRole());

        final Profile profile = profileMapper.toProfile(command, user);

        user.setProfile(profile);

        userRepository.save(user);

        eventPublisher.publish(new UserRegistrationEvent(command.getEmail()));

        return null;
    }

    private void validateCommand(CreateUserCommand command) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(command.getEmail())) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
