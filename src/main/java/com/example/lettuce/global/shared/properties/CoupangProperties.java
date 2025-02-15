package com.example.lettuce.global.shared.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Setter
@Component
@ConfigurationProperties(prefix = "lettuce.crawler.coupang")
public class CoupangProperties {
    private String baseUrl;

    private String listUrl;

    // https://www.coupang.com/vp/products/8185071426?itemId=21686786&vendorItemId=3031645211&src=1191000&spec=10999999&addtag=400&ctag=8185071426&lptag=CFM92302123&itime=20250213165913&pageType=PRODUCT&pageValue=8185071426&wPcid=17376356834974402418444&wRef=&wTime=20250213165913&redirect=landing&mcid=785425a0b30841498cf8b0d3081d1c59&sharesource=sharebutton&style=&isshortened=Y&settlement=N
    // to this
    // https://www.coupang.com/vp/products/8185071426
    public String getBaseUrl(String url) {
        return baseUrl;
    }

    // https://www.coupang.com/np/search?q=아이스크림&channel=user&component=&eventCategory=SRP&trcid=&traid=&sorter=scoreDesc&minPrice=&maxPrice=&priceRange=&filterType=&listSize=36&filter=&isPriceRange=false&brand=&offerCondition=&rating=0&page=9&rocketAll=false&searchIndexingToken=1=8&backgroundColor=
    public String getListUrl(String productName, int page, int offset) {
        return String.format(this.listUrl, productName, page, offset);
    }
}
