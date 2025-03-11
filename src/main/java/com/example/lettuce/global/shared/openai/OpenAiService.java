package com.example.lettuce.global.shared.openai;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.model.Media;
import org.springframework.util.MimeTypeUtils;
import java.util.List;

import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiService {

    private static final OpenAiChatOptions CHAT_OPTIONS = OpenAiChatOptions.builder()
            .model(ChatModel.GPT_4_O_MINI.getValue())
            .build();

    private final OpenAiChatModel chatModel;

    /**
     * Calculate Carbon Footprint by Image
     * 
     * @param prompt     the text prompt to send to OpenAI
     * @param imageBytes the raw image bytes
     * @return String (Carbon Footprint)
     * @throws BaseException if OpenAI API call fails or image is not valid
     */
    public String visionChat(String prompt, byte[] imageBytes) {
        try {
            // Create a ByteArrayResource with the raw image bytes
            ByteArrayResource imageResource = new ByteArrayResource(imageBytes);

            // Create a media object with the JPEG MIME type
            Media media = new Media(MimeTypeUtils.IMAGE_JPEG, imageResource);
            UserMessage userMessage = new UserMessage(prompt, List.of(media));

            return chatModel.call(new Prompt(userMessage, CHAT_OPTIONS)).getResult().getOutput().getText().trim();
        } catch (Exception e) {
            log.error("OpenAI API 호출 중 오류 발생", e);
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }

    /**
     * Calculate Carbon Footprint by Text
     * 
     * @param prompt the text prompt to send to OpenAI
     * @return String (Carbon Footprint)
     * @throws BaseException if OpenAI API call fails or prompt is not valid
     */
    public String textChat(String prompt) {
        try {
            return chatModel.call(new Prompt(prompt, CHAT_OPTIONS)).getResult().getOutput().getText().trim();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }
}
