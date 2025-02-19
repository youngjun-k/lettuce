package com.example.lettuce.domain.carbonfootprint.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.repository.AsyncCarbonFootprintProductRepository;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintProductRepository;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.shared.async.AsyncEventProducer;
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
    private final AsyncEventProducer<CarbonFootPrintProduct> asyncEventProducer;

    private final AsyncCarbonFootprintProductRepository asyncCarbonFootprintProductRepository;

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
    public CarbonFootprintProductResponse calculateFootprintByUrl(String url, User user) {
        CarbonFootprintProductResponse response = carbonFootprintProductRepository.findByUrl(url);

        asyncEventProducer.produce(Collections.singletonList(CarbonFootPrintProduct.of(user.getId(), response)));
        return response;
    }

    @Cacheable(value = "carbon_footprint_product_by_product_name", key = "#name")
    public Page<CarbonFootprintProductResponse> calculateFootprintByName(String name, Pageable pageable, User user) {
        Page<CarbonFootprintProductResponse> responses = carbonFootprintProductRepository.findByName(name, pageable);

        if (responses.isEmpty()) {
            return Page.empty();
        }
        List<CarbonFootPrintProduct> carbonFootprintProducts = responses.stream()
                .map(res -> CarbonFootPrintProduct.of(user.getId(), res))
                .collect(Collectors.toList());

        asyncEventProducer.produce(carbonFootprintProducts);
        return responses;
    }

    public List<CarbonFootprintProductResponse> findFootprintByUserId(Long userId) {
        List<CarbonFootprintProductResponse> responses = asyncCarbonFootprintProductRepository
                .findByProductByUserId(userId);

        return responses;
    }

    private CarbonFootprintRewardResponse convertToCarbonFootprintRewardResponse(String response) {
        try {
            return objectMapper.readValue(response, CarbonFootprintRewardResponse.class);
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.OPENAI_ERROR);
        }
    }

}
