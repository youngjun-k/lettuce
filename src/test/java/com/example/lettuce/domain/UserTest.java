package com.example.lettuce.domain;

import org.junit.Test;

import com.example.lettuce.domain.carbonfootprint.dao.CarbonFootPrintReward;
import com.example.lettuce.domain.user.dao.User;
import com.example.lettuce.domain.user.enums.Level;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

        @Test
        public void normal() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(CarbonFootPrintReward.builder()
                                                .itemName("item")
                                                .awardedPoint(1000)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getLevel()).isEqualTo(Level.NORMAL);
        }

        @Test
        public void silver() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(CarbonFootPrintReward.builder()
                                                .itemName("item")
                                                .awardedPoint(200_001)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getLevel()).isEqualTo(Level.SILVER);
        }

        @Test
        public void gold() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(CarbonFootPrintReward.builder()
                                                .itemName("item")
                                                .awardedPoint(300_001)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getLevel()).isEqualTo(Level.GOLD);
        }

        @Test
        public void vip() {
                // given
                User user = User.builder()
                                .rewards(Collections.singletonList(CarbonFootPrintReward.builder()
                                                .itemName("item")
                                                .awardedPoint(500_001)
                                                .build()))
                                .build();

                // when
                user.levelUp();

                // then
                assertThat(user.getLevel()).isEqualTo(Level.VIP);
        }
}
