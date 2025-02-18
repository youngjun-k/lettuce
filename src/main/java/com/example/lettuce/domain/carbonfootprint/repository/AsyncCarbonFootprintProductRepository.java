package com.example.lettuce.domain.carbonfootprint.repository;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.lettuce.domain.carbonfootprint.service.CarbonFootPrintProduct;

import org.springframework.jdbc.core.RowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class AsyncCarbonFootprintProductRepository {

    private final AsyncMultiProcessor<CarbonFootPrintProduct> asyncMultiProcessor;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CarbonFootPrintProduct> carbonFootPrintProductRowMapper = (rs, rowNum) -> {
        return CarbonFootPrintProduct.of(rs.getString("name"), rs.getString("product_url"),
                rs.getString("thumbnail_image_url"), rs.getBigDecimal("carbon_footprint"));
    };

    @PostConstruct
    public void init() {
        log.info("AsyncCarbonFootprintProductRepository is initialized.");
        asyncMultiProcessor.init(this::saveAll);
    }

    private Optional<CarbonFootPrintProduct> findByProductByName(String productName) {

        String sql = "SELECT * FROM carbon_footprint_products WHERE product_url = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            sql,
                            carbonFootPrintProductRowMapper,
                            productName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    private void saveAll(List<CarbonFootPrintProduct> carbonFootPrintProducts) {
        if (carbonFootPrintProducts.isEmpty()) {
            return;
        }

        StringBuilder sql = new StringBuilder(
                "INSERT INTO carbon_footprint_products (name, product_url, thumbnail_image_url, carbon_footprint) VALUES ");

        for (int i = 0; i < carbonFootPrintProducts.size(); i++) {
            sql.append("(?, ?, ?, ?)");
            if (i < carbonFootPrintProducts.size() - 1) { // 마지막 요소가 아닌 경우에만 콤마 추가
                sql.append(", ");
            }
        }

        jdbcTemplate.update(sql.toString(), ps -> {
            int paramIndex = 1;
            for (CarbonFootPrintProduct carbonFootPrintProduct : carbonFootPrintProducts) {
                ps.setString(paramIndex++, carbonFootPrintProduct.getName());
                ps.setString(paramIndex++, carbonFootPrintProduct.getProductUrl());
                ps.setString(paramIndex++, carbonFootPrintProduct.getThumbnailImageUrl());
                ps.setBigDecimal(paramIndex++, carbonFootPrintProduct.getCarbonFootprint());
            }
        });

    }
}