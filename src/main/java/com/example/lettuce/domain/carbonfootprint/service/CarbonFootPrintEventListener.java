package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintReward;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintRewardRepository;
import com.example.lettuce.global.shared.mapper.CarbonFootPrintMapper;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CarbonFootPrintEventListener implements ApplicationListener<CarbonFootprintImageEvent> {

    private final CarbonFootPrintMapper carbonFootPrintMapper;
    private final CarbonFootprintRewardRepository carbonFootprintRewardRepository;
    private final S3Service s3Service;

    @Override
    public void onApplicationEvent(CarbonFootprintImageEvent event) {

        UploadImageInfo uploadImageInfo = s3Service.uploadCarbonFootprintImage(event.getImage());

        CarbonFootPrintReward carbonFootPrint = carbonFootPrintMapper.toEntity(event.getCarbonFootprintRewardResponse(),
                event.getUser().getId(), uploadImageInfo.ImageUrl());

        carbonFootprintRewardRepository.save(carbonFootPrint);
    }
}
