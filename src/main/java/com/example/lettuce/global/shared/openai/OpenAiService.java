package com.example.lettuce.global.shared.openai;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.model.Media;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.global.shared.constant.PromptConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private static final OpenAiChatOptions CHAT_OPTIONS = OpenAiChatOptions.builder()
            .model(ChatModel.GPT_4_O_MINI.getValue())
            .build();

    private final OpenAiChatModel chatModel;

    /**
     * Calculate Carbon Footprint by Image
     * 
     * @param image
     * @return String (Carbon Footprint)
     * @throws BaseException if OpenAI API call fails or image is not valid
     */
    public String visionChat(MultipartFile image) {
        try {
            UserMessage userMessage = new UserMessage(PromptConstants.CARBON_FOOTPRINT_PROMPT,
                    new Media(MimeTypeUtils.IMAGE_PNG, image.getResource()));

            return chatModel.call(new Prompt(userMessage, CHAT_OPTIONS)).getResult().getOutput().getText().trim();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }

    /**
     * Calculate Carbon Footprint by Text
     * 
     * @param prompt
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
