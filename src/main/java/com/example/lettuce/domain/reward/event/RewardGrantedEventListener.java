package com.example.lettuce.domain.reward.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener for RewardGrantedEvent.
 * This listener handles actions that need to be taken when a reward is granted to a user.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewardGrantedEventListener {

    private final UserRepository userRepository;

    @EventListener
    @Transactional
    public void handleRewardGrantedEvent(RewardGrantedEvent event) {
        log.debug("Processing RewardGrantedEvent: {}", event.getEventId());
        
        // Find the user by ID
        User user = userRepository.findById(Long.valueOf(event.getUserId()))
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        
        // Log the reward details
        log.info("Reward granted to user {}: {} points, {} carbon saved, source: {}",
                user.getId(), event.getPoints(), event.getCarbonSaved(), event.getSource());
        
        // Check if user is eligible for level up based on total rewards
        if (user.availableLevelUp()) {
            // Level up the user
            user.levelUp();
            log.info("User {} leveled up to {}", user.getId(), user.getUserTier());
            
            // Save the updated user
            userRepository.save(user);
        }
    }
} 