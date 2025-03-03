package com.example.lettuce.domain.carbonfootprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lettuce.domain.carbonfootprint.aggregate.RewardHistory;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {
}
