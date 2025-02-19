package com.example.lettuce.domain.carbonfootprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintReward;

public interface CarbonFootprintRewardRepository extends JpaRepository<CarbonFootPrintReward, Long> {
}
