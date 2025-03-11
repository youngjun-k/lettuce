package com.example.lettuce.domain.carbonfootprint.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootprint;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintRepository;
import com.example.lettuce.global.framework.event.DomainEventPublisher;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarbonFootPrintImageEventListener implements ApplicationListener<CarbonFootprintImageEvent> {

    private final S3Service s3Service;
    private final DomainEventPublisher domainEventPublisher;
    private final CarbonFootprintRepository carbonFootprintRepository;

    @Override    
    public void onApplicationEvent(CarbonFootprintImageEvent event) {

        try {
            UploadImageInfo uploadImageInfo = s3Service.uploadCarbonFootprintImage(event.getImageContent(),
                    event.getFilename(), event.getContentType());

            CarbonFootprint carbonFootprint = CarbonFootprint.create(
                    event.getUser(),
                    uploadImageInfo.imageUrl(),
                    event.getCarbonFootprintRewardResponse().productName(),
                    event.getCarbonFootprintRewardResponse().productCategory(),
                    event.getCarbonFootprintRewardResponse().carbonValue(),
                    event.getCarbonFootprintRewardResponse().carbonReduction(),
                    event.getCarbonFootprintRewardResponse().environmentalImpact(), domainEventPublisher);

            carbonFootprintRepository.save(carbonFootprint);

            // RewardHistory rewardHistory = carbonFootPrintMapper.toEntity(
            // event.getCarbonFootprintRewardResponse(),
            // event.getUser().getId(), uploadImageInfo.imageUrl());

            // rewardHistoryRepository.save(rewardHistory);
        } catch (Exception e) {
            log.error("CarbonFootprintImageEvent 처리 중 오류 발생", e);
        }
    }
}
