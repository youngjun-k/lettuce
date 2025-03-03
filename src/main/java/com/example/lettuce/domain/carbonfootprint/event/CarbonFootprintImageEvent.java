package com.example.lettuce.domain.carbonfootprint.event;

import org.springframework.context.ApplicationEvent;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.user.aggregate.User;

import lombok.Getter;

@Getter
public class CarbonFootprintImageEvent extends ApplicationEvent {

    private final CarbonFootprintRewardResponse carbonFootprintRewardResponse;
    // Becuase the listener excutes asynchronously, User Entity will be detached
    // from the persistence context, the persistence context is not shared between
    // threads.
    private final User user;
    private final byte[] imageContent;
    private final String filename;
    private final String contentType;

    public CarbonFootprintImageEvent(Object source, CarbonFootprintRewardResponse carbonFootprintRewardResponse,
            User user, byte[] imageContent, String filename, String contentType) {
        super(source);
        this.carbonFootprintRewardResponse = carbonFootprintRewardResponse;
        this.user = user;
        this.imageContent = imageContent;
        this.filename = filename;
        this.contentType = contentType;
    }
}
