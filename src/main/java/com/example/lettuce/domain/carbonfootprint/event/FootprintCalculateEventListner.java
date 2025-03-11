package com.example.lettuce.domain.carbonfootprint.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import com.example.lettuce.domain.reward.engine.RewardRuleEngine;
import com.example.lettuce.domain.reward.engine.result.RewardCalculationResult;
import com.example.lettuce.domain.reward.repository.RewardWalletRepository;
import com.example.lettuce.domain.reward.aggregate.RewardWallet;
import com.example.lettuce.domain.reward.specification.EligibleForRewardSpec;
import com.example.lettuce.global.framework.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FootprintCalculateEventListner implements ApplicationListener<FootprintCalculatedEvent> {

    private final RewardWalletRepository rewardWalletRepository;
    private final RewardRuleEngine rewardRuleEngine;
    private final EligibleForRewardSpec eligibleForRewardSpec;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public void onApplicationEvent(FootprintCalculatedEvent event) {
        log.debug("Processing FootprintCalculatedEvent: {}", event.getEventId());

        // Check if the carbon footprint is eligible for rewards
        if (!eligibleForRewardSpec.isSatisfiedBy(event.getCarbonFootprint())) {
            log.info("Carbon footprint is not eligible for rewards: {}", event.getAggregateId());
            return;
        }

        // Calculate rewards using the rule engine
        RewardCalculationResult rewardResult = rewardRuleEngine.calculateReward(event.getCarbonFootprint().getUser(),
                event.getCarbonFootprint());

        if (!rewardResult.isEligible() || rewardResult.getPoints() <= 0) {
            log.info("No rewards granted for carbon footprint: {}", event.getAggregateId());
            return;
        }

        // Find or create the user's reward wallet
        RewardWallet wallet = rewardWalletRepository.findByUser(event.getCarbonFootprint().getUser())
                .orElseGet(() -> RewardWallet.create(event.getCarbonFootprint().getUser()));

        // Add points to the wallet
        wallet.addPoints(
                rewardResult.getPoints(),
                rewardResult.getCarbonSaved(),
                rewardResult.getRewardSource(),
                rewardResult.getRewardDescription(),
                eventPublisher);

        // Save the wallet
        rewardWalletRepository.save(wallet);

        log.info("Granted {} points to user {} for carbon footprint {}",
                rewardResult.getPoints(), event.getCarbonFootprint().getUser().getId(), event.getAggregateId());
    }
}