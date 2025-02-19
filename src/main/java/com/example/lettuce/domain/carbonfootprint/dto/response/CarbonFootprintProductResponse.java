package com.example.lettuce.domain.carbonfootprint.dto.response;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CarbonFootprintProductResponse {
        private String name;
        private String productUrl;
        private String thumbnailImageUrl;
        private BigDecimal carbonFootprint;

        public CarbonFootprintProductResponse(String name, String productUrl, String thumbnailImageUrl) {
                this.name = name;
                this.productUrl = productUrl;
                this.thumbnailImageUrl = thumbnailImageUrl;
        }

        public void setCarbonFootprint(BigDecimal carbonFootprint) {
                this.carbonFootprint = carbonFootprint;
        }

        public static CarbonFootprintProductResponse of(long long1, String string, String string2, String string3,
                BigDecimal bigDecimal) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'of'");
        }
}