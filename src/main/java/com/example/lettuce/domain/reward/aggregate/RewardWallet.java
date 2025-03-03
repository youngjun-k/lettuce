package com.example.lettuce.domain.reward.aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.lettuce.domain.reward.event.RewardGrantedEvent;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.event.DomainEventPublisher;
import com.example.lettuce.global.shared.entity.BaseTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RewardWallet is an Aggregate Root that represents a user's reward wallet.
 * It manages the user's reward points and transactions.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reward_wallets", indexes = {
        @Index(name = "idx_reward_wallets_user_id", columnList = "user_id")
})
public class RewardWallet extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_id", nullable = false, unique = true)
    private String aggregateId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_points", nullable = false)
    private int totalPoints;

    @Column(name = "total_carbon_saved", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCarbonSaved;

    @Builder.Default
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RewardTransaction> transactions = new ArrayList<>();

    @Version
    private Long version;

    /**
     * Factory method to create a new RewardWallet instance.
     */
    public static RewardWallet create(User user) {
        return RewardWallet.builder()
                .aggregateId(UUID.randomUUID().toString())
                .user(user)
                .totalPoints(0)
                .totalCarbonSaved(BigDecimal.ZERO)
                .build();
    }

    /**
     * Adds points to the wallet and records a transaction.
     */
    public void addPoints(int points, BigDecimal carbonSaved, String source, String description, DomainEventPublisher eventPublisher) {
        this.totalPoints += points;
        this.totalCarbonSaved = this.totalCarbonSaved.add(carbonSaved);
        
        RewardTransaction transaction = RewardTransaction.builder()
                .wallet(this)
                .points(points)
                .carbonSaved(carbonSaved)
                .source(source)
                .description(description)
                .transactionType(TransactionType.CREDIT)
                .transactionDate(LocalDateTime.now())
                .build();
        
        this.transactions.add(transaction);
        
        // Publish domain event
        RewardGrantedEvent event = new RewardGrantedEvent(
                UUID.randomUUID().toString(),
                this.aggregateId,
                this.user.getId().toString(),
                points,
                carbonSaved,
                source,
                LocalDateTime.now()
        );
        
        eventPublisher.publish(event);
    }

    /**
     * Uses points from the wallet and records a transaction.
     */
    public boolean usePoints(int points, String purpose, String description, DomainEventPublisher eventPublisher) {
        if (this.totalPoints < points) {
            return false;
        }
        
        this.totalPoints -= points;
        
        RewardTransaction transaction = RewardTransaction.builder()
                .wallet(this)
                .points(points)
                .carbonSaved(BigDecimal.ZERO)
                .source(purpose)
                .description(description)
                .transactionType(TransactionType.DEBIT)
                .transactionDate(LocalDateTime.now())
                .build();
        
        this.transactions.add(transaction);
        
        return true;
    }
} 