package com.example.lettuce.domain.carbonfootprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrint;

public interface CarbonFootprintRepository extends JpaRepository<CarbonFootPrint, Long> {
    
}
