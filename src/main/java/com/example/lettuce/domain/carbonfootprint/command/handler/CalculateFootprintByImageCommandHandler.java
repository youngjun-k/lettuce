package com.example.lettuce.domain.carbonfootprint.command.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.command.dto.CalculateFootprintByImageCommand;
import com.example.lettuce.domain.carbonfootprint.event.CarbonFootprintImageEvent;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintProductRepository;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.shared.openai.OpenAiService;
import com.example.lettuce.global.shared.constant.PromptConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CalculateFootprintByImageCommandHandler
        implements CommandHandler<CalculateFootprintByImageCommand, CarbonFootprintRewardResponse> {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    @Qualifier("coupangCrawler")
    private final CarbonFootprintProductRepository carbonFootprintProductRepository;
    private final OpenAiService openAiService;

    /**
     * Calculate Carbon Footprint by Image.
     * <p>
     * This method sends the image to the OpenAiService for analysis and then
     * maps the response to a CarbonFootprintRewardResponse. It also publishes
     * an event for any further asynchronous processing.
     * </p>
     *
     * @param image the multipart image file to be analyzed
     * @param user  the current user performing the operation
     * @return CarbonFootprintRewardResponse that details the rewards based on the
     *         calculated carbon footprint
     */
    @Override
    public CarbonFootprintRewardResponse handle(CalculateFootprintByImageCommand command) {
        String response = openAiService.visionChat(PromptConstants.CARBON_FOOTPRINT_PROMPT, command.image());
        CarbonFootprintRewardResponse carbonFootprintRewardResponse = convertToCarbonFootprintRewardResponse(response);

        try {
            eventPublisher.publishEvent(new CarbonFootprintImageEvent(this, carbonFootprintRewardResponse,
                    command.user(), command.image().getBytes(), command.image().getOriginalFilename(),
                    command.image().getContentType()));
        } catch (IOException e) {
            throw new BaseException(ErrorCode.IMAGE_PROCESSING_ERROR);
        }

        return carbonFootprintRewardResponse;
    }

    private CarbonFootprintRewardResponse convertToCarbonFootprintRewardResponse(String response) {
        try {
            return objectMapper.readValue(response, CarbonFootprintRewardResponse.class);
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }

}
