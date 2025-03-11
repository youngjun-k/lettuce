package com.example.lettuce.domain.reward.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lettuce.domain.reward.aggregate.RewardWallet;
import com.example.lettuce.domain.user.aggregate.User;

/**
 * Repository for accessing RewardWallet entities.
 */
@Repository
public interface RewardWalletRepository extends JpaRepository<RewardWallet, Long> {
    
    /**
     * Finds a reward wallet by its aggregate ID.
     * 
     * @param aggregateId The aggregate ID
     * @return The reward wallet, if found
     */
    Optional<RewardWallet> findByAggregateId(String aggregateId);
    
    /**
     * Finds a reward wallet for a specific user.
     * 
     * @param user The user
     * @return The reward wallet, if found
     */
    Optional<RewardWallet> findByUser(User user);
    
    /**
     * Finds a reward wallet for a specific user ID.
     * 
     * @param userId The user ID
     * @return The reward wallet, if found
     */
    @Query("SELECT rw FROM RewardWallet rw WHERE rw.user.id = :userId")
    Optional<RewardWallet> findByUserId(@Param("userId") Long userId);
} 