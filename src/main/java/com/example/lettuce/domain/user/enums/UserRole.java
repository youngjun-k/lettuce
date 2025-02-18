package com.example.lettuce.domain.user.enums;

public enum UserRole {
    // Core roles
    ADMIN("ROLE_ADMIN"),
    CLIENT("ROLE_CLIENT"),
    GUEST("ROLE_GUEST"),

    // Specialized roles
    FARMER("ROLE_FARMER"),
    BUSINESS_PARTNER("ROLE_BUSINESS_PARTNER"),
    AFFILIATE_PARTNER("ROLE_AFFILIATE_PARTNER"),
    MODERATOR("ROLE_MODERATOR"),
    PREMIUM_CLIENT("ROLE_PREMIUM_CLIENT"),
    DATA_AUDITOR("ROLE_DATA_AUDITOR");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
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