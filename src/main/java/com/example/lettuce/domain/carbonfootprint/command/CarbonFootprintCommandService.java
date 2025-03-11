package com.example.lettuce.domain.carbonfootprint.command;

import org.springframework.stereotype.Service;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.command.dto.CalculateFootprintByImageCommand;
import com.example.lettuce.global.framework.cqrs.CommandBus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarbonFootprintCommandService {

    private final CommandBus commandBus;

    public CarbonFootprintRewardResponse calculateFootprintByImage(CalculateFootprintByImageCommand command) {
        return commandBus.dispatch(command);
    }
}
