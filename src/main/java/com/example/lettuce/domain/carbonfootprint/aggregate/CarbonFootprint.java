package com.example.lettuce.domain.carbonfootprint.aggregate;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.lettuce.domain.carbonfootprint.event.FootprintCalculatedEvent;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.event.DomainEventPublisher;
import com.example.lettuce.global.shared.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CarbonFootprint is an Aggregate Root that represents a carbon footprint
 * calculation for a specific user and product.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "carbon_footprints", indexes = {
                @Index(name = "idx_carbon_footprints_user_id_created_at", columnList = "user_id, created_at")
})
public class CarbonFootprint extends BaseTime {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "aggregate_id", nullable = false, unique = true)
        private String aggregateId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        @JsonIgnore
        private User user;

        @Column(name = "image_url", length = 255)
        private String imageUrl;

        @Column(name = "product_name", nullable = false, length = 255)
        private String productName;

        @Column(name = "product_category", nullable = false, length = 100)
        private String productCategory;

        @Column(name = "carbon_value", nullable = false, precision = 10, scale = 2)
        private BigDecimal carbonValue;

        @Column(name = "carbon_reduction", nullable = false, precision = 10, scale = 2)
        private BigDecimal carbonReduction;

        @Column(name = "environmental_impact", nullable = false, length = 1000)
        private String environmentalImpact;

        @Version
        private Long version;

        /**
         * Factory method to create a new CarbonFootprint instance.
         */
        public static CarbonFootprint create(
                        User user,
                        String imageUrl,
                        String productName,
                        String productCategory,
                        BigDecimal carbonValue,
                        BigDecimal carbonReduction,
                        String environmentalImpact,
                        DomainEventPublisher eventPublisher) {

                CarbonFootprint footprint = CarbonFootprint.builder()
                                .aggregateId(UUID.randomUUID().toString())
                                .user(user)
                                .imageUrl(imageUrl)
                                .productName(productName)
                                .productCategory(productCategory)
                                .carbonValue(carbonValue)
                                .carbonReduction(carbonReduction)
                                .environmentalImpact(environmentalImpact)
                                .build();

                publishFootprintCalculatedEvent(footprint, eventPublisher);
                return footprint;
        }

        /**
         * Updates the carbon footprint calculation with new values.
         */
        public void update(
                        BigDecimal carbonValue,
                        BigDecimal carbonReduction,
                        String environmentalImpact,
                        DomainEventPublisher eventPublisher) {

                this.carbonValue = carbonValue;
                this.carbonReduction = carbonReduction;
                this.environmentalImpact = environmentalImpact;

                publishFootprintCalculatedEvent(this, eventPublisher);
        }

        /**
         * Publishes a FootprintCalculatedEvent for the given footprint.
         */
        private static void publishFootprintCalculatedEvent(CarbonFootprint footprint,
                        DomainEventPublisher eventPublisher) {
                if (eventPublisher != null) {
                        eventPublisher.publish(new FootprintCalculatedEvent(footprint));
                }
        }
}