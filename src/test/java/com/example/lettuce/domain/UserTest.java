package com.example.lettuce.domain;

import org.junit.Test;

import com.example.lettuce.domain.carbonfootprint.aggregate.RewardHistory;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.aggregate.enums.UserTier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

        @Test
        public void normal() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(RewardHistory.builder()
                                                .itemName("item")
                                                .awardedPoint(1000)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getUserTier()).isEqualTo(UserTier.NORMAL);
        }

        @Test
        public void silver() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(RewardHistory.builder()
                                                .itemName("item")
                                                .awardedPoint(200_001)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getUserTier()).isEqualTo(UserTier.SILVER);
        }

        @Test
        public void gold() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(RewardHistory.builder()
                                                .itemName("item")
                                                .awardedPoint(300_001)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getUserTier()).isEqualTo(UserTier.GOLD);
        }

        @Test
        public void vip() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(RewardHistory.builder()
                                                .itemName("item")
                                                .awardedPoint(500_001)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getUserTier()).isEqualTo(UserTier.VIP);
        }
}
