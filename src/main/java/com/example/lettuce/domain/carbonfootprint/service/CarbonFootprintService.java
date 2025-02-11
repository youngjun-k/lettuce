package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintResponse;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.shared.constant.PromptConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.model.Media;

@Service
@RequiredArgsConstructor
public class CarbonFootprintService {

    private static final OpenAiChatOptions CHAT_OPTIONS = OpenAiChatOptions.builder()
            .model(ChatModel.GPT_4_O_MINI.getValue())
            .build();

    private final ObjectMapper objectMapper;

    private final OpenAiChatModel chatModel;

    private final ApplicationEventPublisher eventPublisher;

    public CarbonFootprintResponse calculateFootprint(MultipartFile image, User user) {
        try {

            UserMessage userMessage = new UserMessage(PromptConstants.CARBON_FOOTPRINT_PROMPT,
                    new Media(MimeTypeUtils.IMAGE_PNG, image.getResource()));

            ChatResponse response = chatModel.call(new Prompt(userMessage, CHAT_OPTIONS));

            CarbonFootprintResponse carbonFootprintResponse = objectMapper.readValue(
                    response.getResult().getOutput().getText().trim(),
                    CarbonFootprintResponse.class);

            eventPublisher.publishEvent(new CarbonFootprintEvent(this, carbonFootprintResponse, user, image));

            return carbonFootprintResponse;

        } catch (Exception e) {
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }

}
