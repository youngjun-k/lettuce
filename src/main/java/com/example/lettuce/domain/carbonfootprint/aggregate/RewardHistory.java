package com.example.lettuce.domain.carbonfootprint.aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.shared.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "carbon_footprint_rewards", indexes = {
        @Index(name = "idx_carbon_footprints_user_id", columnList = "user_id, created_at")
})
public class RewardHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "image_url", nullable = true, length = 255, columnDefinition = "VARCHAR(255) COMMENT '이미지 URL'")
    private String imageUrl;

    @Column(name = "item_category", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '상품 카테고리'")
    private String itemCategory;

    @Column(name = "item_name", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '상품 이름'")
    private String itemName;

    @Column(name = "description", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '상품 설명'")
    private String description;

    @Column(name = "saved_carbon_footprint", nullable = false, columnDefinition = "DECIMAL(10,2) COMMENT '절약된 Co2 배출량 (kg)'")
    private BigDecimal savedCarbonFootprint;

    @Column(name = "awarded_point", nullable = false, columnDefinition = "INT COMMENT '획득 포인트'")
    private int awardedPoint;

    @Column(name = "used_at", nullable = true, columnDefinition = "DATETIME COMMENT '사용 일시'")
    private LocalDateTime usedAt;
}