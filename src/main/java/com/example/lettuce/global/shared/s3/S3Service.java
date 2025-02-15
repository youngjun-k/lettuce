package com.example.lettuce.global.shared.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;

    public static final String CATEGORY_MEMBER = "user";
    public static final String CATEGORY_CARBON_FOOTPRINT = "carbon-footprint";

    public UploadImageInfo uploadMemberProfileImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_MEMBER, image);
    }

    public UploadImageInfo uploadCarbonFootprintImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_CARBON_FOOTPRINT, image);
    }
}
