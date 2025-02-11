package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrint;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintRepository;
import com.example.lettuce.global.shared.mapper.CarbonFootPrintMapper;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CarbonFootPrintEventListener implements ApplicationListener<CarbonFootprintEvent> {

    private final CarbonFootPrintMapper carbonFootPrintMapper;
    private final CarbonFootprintRepository carbonFootprintRepository;
    private final S3Service s3Service;

    @Override
    public void onApplicationEvent(CarbonFootprintEvent event) {

        UploadImageInfo uploadImageInfo = s3Service.uploadCarbonFootprintImage(event.getImage());

        CarbonFootPrint carbonFootPrint = carbonFootPrintMapper.toEntity(event.getCarbonFootprintResponse(),
                event.getUser().getId(), uploadImageInfo.ImageUrl());

        carbonFootprintRepository.save(carbonFootPrint);
    }
}
