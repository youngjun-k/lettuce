package com.example.lettuce.domain.carbonfootprint.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintProductRepository;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.shared.constant.PromptConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.example.lettuce.global.shared.openai.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarbonFootprintService {

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
    public CarbonFootprintRewardResponse calculateFootprintByImage(MultipartFile image, User user) {
        String response = openAiService.visionChat(PromptConstants.CARBON_FOOTPRINT_PROMPT, image);
        CarbonFootprintRewardResponse carbonFootprintRewardResponse = convertToCarbonFootprintRewardResponse(response);

        try {
            eventPublisher.publishEvent(new CarbonFootprintImageEvent(this, carbonFootprintRewardResponse, user,
                    image.getBytes(), image.getOriginalFilename(), image.getContentType()));
        } catch (IOException e) {
            throw new BaseException(ErrorCode.IMAGE_PROCESSING_ERROR);
        }
        
        return carbonFootprintRewardResponse;
    }

    /**
     * Calculate Carbon Footprint by URL.
     * <p>
     * This method retrieves a CarbonFootPrintProduct by URL from the crawler
     * and maps it to a CarbonFootprintProductResponse.
     * </p>
     *
     * @param url the URL of the product
     * @return a CarbonFootprintProductResponse containing product details and
     *         carbon footprint
     */
    @Cacheable(value = "carbon_footprint_product_by_product_id", key = "#url.split('/')[4]")
    public CarbonFootprintProductResponse calculateFootprintByUrl(String url) {
        CarbonFootprintProductResponse carbonFootprintProduct = carbonFootprintProductRepository.findByUrl(url);

        eventPublisher.publishEvent(new CarbonFootprintProductEvent(this, carbonFootprintProduct));

        return carbonFootprintProduct;
    }

    @Cacheable(value = "carbon_footprint_product_by_product_name", key = "#name")
    public Page<CarbonFootprintProductResponse> calculateFootprintByName(String name, Pageable pageable) {
        return carbonFootprintProductRepository.findByName(name, pageable);
    }

    private CarbonFootprintRewardResponse convertToCarbonFootprintRewardResponse(String response) {
        try {
            return objectMapper.readValue(response, CarbonFootprintRewardResponse.class);
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }
}
