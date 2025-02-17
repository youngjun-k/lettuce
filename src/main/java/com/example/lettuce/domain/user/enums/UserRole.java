package com.example.lettuce.domain.user.enums;

import org.mapstruct.factory.Mappers;

import com.example.lettuce.domain.user.service.ClientProfileUpdateStrategy;
import com.example.lettuce.domain.user.service.FarmerProfileUpdateStrategy;
import com.example.lettuce.domain.user.service.PartnerProfileUpdateStrategy;
import com.example.lettuce.domain.user.service.ProfileUpdateStrategy;
import com.example.lettuce.global.shared.mapper.ClientProfileMapper;
import com.example.lettuce.global.shared.mapper.FarmerProfileMapper;
import com.example.lettuce.global.shared.mapper.PartnerProfileMapper;
import com.example.lettuce.global.shared.mapper.ProfileMapper;

public enum UserRole {
    // Core roles
    ADMIN("ROLE_ADMIN", null, null),
    CLIENT("ROLE_CLIENT", Mappers.getMapper(ClientProfileMapper.class), new ClientProfileUpdateStrategy()),
    GUEST("ROLE_GUEST", null, null),

    // Specialized roles
    FARMER("ROLE_FARMER", Mappers.getMapper(FarmerProfileMapper.class), new FarmerProfileUpdateStrategy()),
    BUSINESS_PARTNER("ROLE_BUSINESS_PARTNER", Mappers.getMapper(PartnerProfileMapper.class),
            new PartnerProfileUpdateStrategy()),
    AFFILIATE_PARTNER("ROLE_AFFILIATE_PARTNER", Mappers.getMapper(PartnerProfileMapper.class),
            new PartnerProfileUpdateStrategy()),
    MODERATOR("ROLE_MODERATOR", null, null),
    PREMIUM_CLIENT("ROLE_PREMIUM_CLIENT", null, null),
    DATA_AUDITOR("ROLE_DATA_AUDITOR", null, null);

    private final String roleName;
    private final ProfileMapper<?, ?, ?> profileMapper;
    private final ProfileUpdateStrategy profileUpdateStrategy;

    UserRole(String roleName, ProfileMapper<?, ?, ?> profileMapper,
            ProfileUpdateStrategy profileUpdateStrategy) {
        this.roleName = roleName;
        this.profileMapper = profileMapper;
        this.profileUpdateStrategy = profileUpdateStrategy;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public ProfileMapper<?, ?, ?> getProfileMapper() {
        return this.profileMapper;
    }

    public ProfileUpdateStrategy getProfileUpdateStrategy() {
        return this.profileUpdateStrategy;
    }

    // Helper method to convert from String to enum
    public static UserRole fromRoleName(String roleName) {
        try {
            return UserRole.valueOf(roleName.replace("ROLE_", "").toUpperCase());
        } catch (IllegalArgumentException ex) {
            // Handle unknown roles gracefully
            return GUEST; // Default fallback
        }
    }

    // For Spring Security integration
    public String getAuthority() {
        return roleName;
    }

    // Simple name for UI display (without "ROLE_")
    public String getSimpleRoleName() {
        return roleName.replace("ROLE_", "");
    }
}