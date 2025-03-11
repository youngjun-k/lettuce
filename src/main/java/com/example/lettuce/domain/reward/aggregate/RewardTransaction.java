package com.example.lettuce.domain.reward.aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.lettuce.global.shared.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

/**
 * RewardTransaction represents a transaction in the reward wallet.
 * It records the points earned or spent by the user.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reward_transactions", indexes = {
        @Index(name = "idx_reward_transactions_wallet_id", columnList = "wallet_id"),
        @Index(name = "idx_reward_transactions_transaction_date", columnList = "transaction_date")
})
public class RewardTransaction extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    @JsonIgnore
    private RewardWallet wallet;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "carbon_saved", nullable = false, precision = 10, scale = 2)
    private BigDecimal carbonSaved;

    @Column(name = "source", nullable = false, length = 100)
    private String source;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 10)
    private TransactionType transactionType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
} 