package com.example.lettuce.domain.user.entity;

import java.time.LocalDateTime;

import com.example.lettuce.domain.user.enums.UserRole;
import com.example.lettuce.domain.user.service.ClientProfileStrategy;
import com.example.lettuce.domain.user.service.FarmerProfileStrategy;
import com.example.lettuce.domain.user.service.PartnerProfileStrategy;
import com.example.lettuce.domain.user.service.ProfileStrategy;
import com.example.lettuce.global.shared.entity.BaseTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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

    @JsonIgnore
    @Column(name = "password", length = 60, nullable = false, columnDefinition = "varchar(60) comment '회원 비밀번호'")
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true comment '회원 활성화 여부'")
    private boolean enabled;

    @Column(name = "verified", nullable = false, columnDefinition = "boolean default false comment '이메일 인증 여부'")
    private boolean verified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false, columnDefinition = "varchar(20) default 'CLIENT' comment '회원 역할'")
    private UserRole role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id")
    private ClientProfile clientProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_profile_id")
    private FarmerProfile farmerProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_profile_id")
    private PartnerProfile partnerProfile;

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

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
