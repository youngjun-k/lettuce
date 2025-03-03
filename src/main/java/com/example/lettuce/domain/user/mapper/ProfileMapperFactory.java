package com.example.lettuce.domain.user.mapper;

import com.example.lettuce.domain.user.aggregate.enums.UserRole;
import com.example.lettuce.domain.user.stragies.ClientProfileStrategy;
import com.example.lettuce.domain.user.stragies.FarmerProfileStrategy;
import com.example.lettuce.domain.user.stragies.PartnerProfileStrategy;
import com.example.lettuce.domain.user.stragies.ProfileStrategy;

import org.mapstruct.factory.Mappers;

public class ProfileMapperFactory {
    public static ProfileMapper<?, ?, ?> getProfileMapper(UserRole role) {
        return switch (role) {
            case CLIENT -> Mappers.getMapper(ClientProfileMapper.class);
            case BUSINESS_PARTNER, AFFILIATE_PARTNER -> Mappers.getMapper(PartnerProfileMapper.class);
            case FARMER -> Mappers.getMapper(FarmerProfileMapper.class);
            default -> throw new IllegalArgumentException("Invalid user role: " + role);
        };
    }

    public static ProfileStrategy getProfileStrategy(UserRole role) {
        return switch (role) {
            case CLIENT -> new ClientProfileStrategy();
            case BUSINESS_PARTNER, AFFILIATE_PARTNER -> new PartnerProfileStrategy();
            case FARMER -> new FarmerProfileStrategy();
            default -> throw new IllegalArgumentException("Invalid user role: " + role);
        };
    }
}
