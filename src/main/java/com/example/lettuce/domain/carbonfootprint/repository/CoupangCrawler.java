package com.example.lettuce.domain.carbonfootprint.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintProduct;
import com.example.lettuce.global.shared.constant.PromptConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.example.lettuce.global.shared.openai.OpenAiService;
import com.example.lettuce.global.shared.properties.CoupangProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoupangCrawler implements CarbonFootprintProductRepository {

    private final OpenAiService openAiService;
    private final CoupangProperties coupangProperties;

    @Override
    public CarbonFootPrintProduct findByUrl(String url) {
        Element bodyElement = getBodyElement(url);
        if (bodyElement == null) {
            throw new BaseException(ErrorCode.NOT_FOUND);
        }

        String title = bodyElement.select("h1.ProductInfo_title__fLscZ").first().text();

        String thumbnailUrl = bodyElement.select("#MWEB_PRODUCT_DETAIL_ITEM_THUMBNAILS img").attr("src");

        String carbonFootprint = openAiService
                .textChat(PromptConstants.CARBON_FOOTPRINT_BY_TEXT_PROMPT.replace("{productName}", title));

        return CarbonFootPrintProduct.builder()
                .name(title)
                .url(url)
                .thumbnailImageUrl(thumbnailUrl)
                .carbonFootprint(carbonFootprint)
                .build();
    }


    private Element getBodyElement(String url) {
        try {
            return Jsoup.connect(url)
                    .followRedirects(true)
                    .userAgent(
                            "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Mobile Safari/537.36")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .get()
                    .body();

        } catch (IOException e) {
            log.error("Error in crawling Coupang", e);
            throw new BaseException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}