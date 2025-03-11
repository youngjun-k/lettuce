package com.example.lettuce.domain.user.aggregate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.lettuce.domain.carbonfootprint.aggregate.RewardHistory;
import com.example.lettuce.domain.user.aggregate.enums.UserRole;
import com.example.lettuce.domain.user.aggregate.enums.UserTier;
import com.example.lettuce.domain.user.command.dto.CreateUserCommand;
import com.example.lettuce.global.shared.entity.BaseTime;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email_deleted_at", columnList = "email, deleted_at")
})
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 100, nullable = false, unique = true, columnDefinition = "varchar(100) comment '회원 이메일'")
    private String email;

    @Convert(converter = PasswordConverter.class)
    @Column(name = "password", length = 60, nullable = false, columnDefinition = "varchar(60) comment '회원 비밀번호'")
    private Password password;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true comment '회원 활성화 여부'")
    private boolean enabled;

    @Column(name = "verified", nullable = false, columnDefinition = "boolean default false comment '이메일 인증 여부'")
    private boolean verified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false, columnDefinition = "varchar(20) default 'CLIENT' comment '회원 역할'")
    private UserRole role;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_tier", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'NORMAL' COMMENT '회원 레벨'")
    private UserTier userTier = UserTier.NORMAL;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    private ClientProfile clientProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    private FarmerProfile farmerProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    private PartnerProfile partnerProfile;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<RewardHistory> rewards = new ArrayList<>();

    public Profile getProfile() {
        return switch (this.role) {
            case CLIENT -> clientProfile;
            case BUSINESS_PARTNER, AFFILIATE_PARTNER -> partnerProfile;
            case FARMER -> farmerProfile;
            default -> throw new IllegalArgumentException("Invalid user role: " + this.role);
        };
    }

    public void setProfile(Profile profile) {
        switch (this.role) {
            case CLIENT -> this.clientProfile = (ClientProfile) profile;
            case BUSINESS_PARTNER, AFFILIATE_PARTNER -> this.partnerProfile = (PartnerProfile) profile;
            case FARMER -> this.farmerProfile = (FarmerProfile) profile;
            default -> throw new IllegalArgumentException("Invalid user role: " + this.role);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void verifyEmail() {
        if (this.verified) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }
        this.verified = true;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password.changePassword(password);
    }

    public int getTotalAmount() {
        return this.rewards.stream()
                .mapToInt(RewardHistory::getAwardedPoint)
                .sum();
    }

    public boolean availableLevelUp() {
        return UserTier.availableLevelUp(this.userTier, this.getTotalAmount());
    }

    public UserTier levelUp() {
        UserTier nextTier = UserTier.getNextLevel(this.getTotalAmount());
        this.userTier = nextTier;
        return nextTier;
    }

    public static User create(CreateUserCommand command) {
        return User.builder()
                .email(command.getEmail())
                .password(new Password(command.getPassword()))
                .role(command.getRole())
                .enabled(true)
                .verified(true)
                .build();
    }
}
