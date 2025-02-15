package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.user.entity.User;

import lombok.Getter;

@Getter
public class CarbonFootprintImageEvent extends ApplicationEvent {

    private final CarbonFootprintRewardResponse carbonFootprintRewardResponse;
    // Becuase the listener excutes asynchronously, User Entity will be detached
    // from the persistence context, the persistence context is not shared between
    // threads.
    private final User user;
    private final MultipartFile image;

    public CarbonFootprintImageEvent(Object source, CarbonFootprintRewardResponse carbonFootprintRewardResponse,
            User user, MultipartFile image) {
        super(source);
        this.carbonFootprintRewardResponse = carbonFootprintRewardResponse;
        this.user = user;
        this.image = image;
    }
}
