package com.example.lettuce.domain.carbonfootprint.repository;

import jakarta.annotation.PostConstruct;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.service.CarbonFootPrintProduct;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import org.springframework.jdbc.core.RowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class AsyncCarbonFootprintProductRepository {

    private final AsyncMultiProcessor<CarbonFootPrintProduct> asyncMultiProcessor;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CarbonFootprintProductResponse> carbonFootPrintProductRowMapper = (rs, rowNum) -> {

        return CarbonFootprintProductResponse.of(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("product_url"),
                rs.getString("thumbnail_image_url"),
                rs.getBigDecimal("carbon_footprint"));
    };

    @PostConstruct
    public void init() {
        log.info("AsyncCarbonFootprintProductRepository is initialized.");
        asyncMultiProcessor.init(this::saveAll);
    }

    public List<CarbonFootprintProductResponse> findByProductByUserId(Long userId) {

        String sql = "SELECT * FROM carbon_footprint_products WHERE user_id = ?";
        try {
            return jdbcTemplate.query(sql, carbonFootPrintProductRowMapper, userId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private void saveAll(List<CarbonFootPrintProduct> carbonFootPrintProducts) {
        if (carbonFootPrintProducts.isEmpty()) {
            return;
        }

        try {
            StringBuilder sql = new StringBuilder(
                    "INSERT INTO carbon_footprint_products (user_id, name, product_url, thumbnail_image_url, carbon_footprint) VALUES ");

            for (int i = 0; i < carbonFootPrintProducts.size(); i++) {
                sql.append("(?, ?, ?, ?, ?)");
                // Add comma except for the last element
                if (i < carbonFootPrintProducts.size() - 1) {
                    sql.append(", ");
                }
            }

            jdbcTemplate.update(sql.toString(), ps -> {
                int paramIndex = 1;
                for (CarbonFootPrintProduct carbonFootPrintProduct : carbonFootPrintProducts) {
                    ps.setLong(paramIndex++, carbonFootPrintProduct.getUserId());
                    ps.setString(paramIndex++, carbonFootPrintProduct.getName());
                    ps.setString(paramIndex++, carbonFootPrintProduct.getProductUrl());
                    ps.setString(paramIndex++, carbonFootPrintProduct.getThumbnailImageUrl());
                    ps.setBigDecimal(paramIndex++, carbonFootPrintProduct.getCarbonFootprint());
                }
            });
        } catch (Exception e) {
            log.error("Error saving carbon footprint products: {}", e.getMessage());
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}