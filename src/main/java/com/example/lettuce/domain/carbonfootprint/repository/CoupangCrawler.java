package com.example.lettuce.domain.carbonfootprint.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.global.shared.constant.PromptConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.example.lettuce.global.shared.openai.OpenAiService;
import com.example.lettuce.global.shared.properties.CoupangProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoupangCrawler implements CarbonFootprintProductRepository {

    private final OpenAiService openAiService;
    private final CoupangProperties coupangProperties;

    private static final String DECIMAL_SEPARATE_BY_COMMA_REGEX = "\\d+\\.\\d+(?:,\\s*\\d+\\.\\d+)*";
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Mobile Safari/537.36";
    private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.9";

    @Override
    public CarbonFootprintProductResponse findByUrl(String url) {
        Element bodyElement = getBodyElement(url);
        if (bodyElement == null) {
            throw new BaseException(ErrorCode.NOT_FOUND);
        }

        String title = bodyElement.select("h1.ProductInfo_title__fLscZ").first().text();
        String thumbnailUrl = bodyElement.select("#MWEB_PRODUCT_DETAIL_ITEM_THUMBNAILS img").attr("src");
        String carbonFootprint = getCarbonFootprint(title);

        return new CarbonFootprintProductResponse(title, url, thumbnailUrl, new BigDecimal(carbonFootprint));
    }

    @Override
    public Page<CarbonFootprintProductResponse> findByName(String name, Pageable pageable) {
        String url = coupangProperties.getListUrl(name, pageable.getPageNumber(), pageable.getPageSize());
        Element bodyElement = getBodyElement(url);

        if (bodyElement == null) {
            return new PageImpl<>(Collections.emptyList());
        }

        Elements productItems = bodyElement.select("li.plp-default__item");
        if (productItems.isEmpty()) {
            return new PageImpl<>(Collections.emptyList());
        }

        List<CarbonFootprintProductResponse> responses = productItems.stream()
                .map(this::createProductResponse)
                .collect(Collectors.toList());

        List<BigDecimal> carbonFootprints = getCarbonFootprintList(
                responses.stream()
                        .map(CarbonFootprintProductResponse::getName)
                        .collect(Collectors.joining(", ")));

        IntStream.range(0, Math.min(responses.size(), carbonFootprints.size()))
                .forEach(i -> responses.get(i).setCarbonFootprint(carbonFootprints.get(i)));

        return new PageImpl<>(responses, pageable, responses.size());
    }

    private CarbonFootprintProductResponse createProductResponse(Element item) {
        String title = item.select("strong.title").text();
        return new CarbonFootprintProductResponse(title, getProductUrl(item), getImageUrl(item));
    }

    private String getProductUrl(Element item) {
        String productUrl = item.select("a.sdw-similar-product-go-to-sdp-click").attr("href");
        return coupangProperties.getBaseProductUrl(productUrl);
    }

    private String getImageUrl(Element item) {
        String imageUrl = item.select("img.loading").attr("src");
        return imageUrl.startsWith("//") ? "https:" + imageUrl : imageUrl;
    }

    private String getCarbonFootprint(String productName) {
        return openAiService.textChat(
                PromptConstants.CARBON_FOOTPRINT_BY_TEXT_PROMPT.replace("{productName}", productName));
    }

    private List<BigDecimal> getCarbonFootprintList(String productNames) {
        try {
            int productCount = productNames.split(", ").length;
            String response = openAiService.textChat(
                    PromptConstants.CARBON_FOOTPRINT_BY_TEXT_PROMPT_LIST.replace("{productNames}", productNames));

            if (!response.matches(DECIMAL_SEPARATE_BY_COMMA_REGEX)) {
                return Collections.nCopies(productCount, BigDecimal.ZERO);
            }

            return Arrays.stream(response.split(", "))
                    .map(String::trim)
                    .map(s -> s.isEmpty() ? BigDecimal.ZERO : new BigDecimal(s))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting carbon footprints", e);
            return Collections.emptyList();
        }
    }

    private Element getBodyElement(String url) {
        try {
            return Jsoup.connect(url)
                    .followRedirects(true)
                    .userAgent(USER_AGENT)
                    .header("Accept-Language", ACCEPT_LANGUAGE)
                    .get()
                    .body();
        } catch (IOException e) {
            log.error("Error crawling Coupang: {}", e.getMessage());
            throw new BaseException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}