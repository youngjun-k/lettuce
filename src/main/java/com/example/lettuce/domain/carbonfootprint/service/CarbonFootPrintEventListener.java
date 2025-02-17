package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintReward;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintRewardRepository;
import com.example.lettuce.global.shared.mapper.CarbonFootPrintMapper;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarbonFootPrintEventListener implements ApplicationListener<CarbonFootprintImageEvent> {

    private final CarbonFootPrintMapper carbonFootPrintMapper;
    private final CarbonFootprintRewardRepository carbonFootprintRewardRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void onApplicationEvent(CarbonFootprintImageEvent event) {

        try {
            System.out.println("CarbonFootprintImageEvent 처리 시작");
            UploadImageInfo uploadImageInfo = s3Service.uploadCarbonFootprintImage(event.getImageContent(),
                    event.getFilename(), event.getContentType());

            CarbonFootPrintReward carbonFootPrint = carbonFootPrintMapper.toEntity(
                    event.getCarbonFootprintRewardResponse(),
                    event.getUser().getId(), uploadImageInfo.imageUrl());

            carbonFootprintRewardRepository.save(carbonFootPrint);
        } catch (Exception e) {
            log.error("CarbonFootprintImageEvent 처리 중 오류 발생", e);
        }
    }
}
