package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintResponse;
import com.example.lettuce.domain.user.entity.User;

import lombok.Getter;

@Getter
public class CarbonFootprintEvent extends ApplicationEvent {

    private final CarbonFootprintResponse carbonFootprintResponse;
    // Becuase the listener excutes asynchronously, User Entity will be detached
    // from the persistence context, the persistence context is not shared between
    // threads.
    private final User user;
    private final MultipartFile image;

    public CarbonFootprintEvent(Object source, CarbonFootprintResponse carbonFootprintResponse, User user,
            MultipartFile image) {
        super(source);
        this.carbonFootprintResponse = carbonFootprintResponse;
        this.user = user;
        this.image = image;
    }
}
