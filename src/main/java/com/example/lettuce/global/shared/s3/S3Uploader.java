package com.example.lettuce.global.shared.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final int UUID_PREFIX_LENGTH = 16;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.url}")
    private String cloudFrontUrl;

    public UploadImageInfo uploadMultipartFileToBucket(String category, MultipartFile file) {
        validateImageContentType(file.getContentType());
        String filePath = buildFilePath(category, file.getName());
        ObjectMetadata metadata = createMetadata(file.getContentType(), file.getSize());

        try (var inputStream = file.getInputStream()) {
            return uploadToS3(filePath, inputStream, metadata);
        } catch (Exception e) {
            logAndThrowError(category, file.getName(), e);
            return null; // Never reached due to exception
        }
    }

    public UploadImageInfo uploadBytesToBucket(String category, byte[] imageContent, String filename,
            String contentType) {
        validateImageContentType(contentType);
        String filePath = buildFilePath(category, filename);
        ObjectMetadata metadata = createMetadata(contentType, imageContent.length);

        try (var inputStream = new ByteArrayInputStream(imageContent)) {
            return uploadToS3(filePath, inputStream, metadata);
        } catch (Exception e) {
            logAndThrowError(category, filename, e);
            return null; // Never reached due to exception
        }
    }

    public void uploadParquetFileToS3(String category, String filename, File file) {
        try (var inputStream = new FileInputStream(file)) {
            uploadToS3(buildFilePath(category, filename), inputStream, null);
        } catch (Exception e) {
            logAndThrowError(category, filename, e);
        }
    }

    private ObjectMetadata createMetadata(String contentType, long contentLength) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(contentLength);
        return metadata;
    }

    private UploadImageInfo uploadToS3(String filePath, InputStream inputStream, ObjectMetadata metadata) {
        try {
            PutObjectRequest request = new PutObjectRequest(bucket, filePath, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);
            return new UploadImageInfo(getCloudFrontUrl(filePath));
        } catch (Exception e) {
            logAndThrowError(filePath, "unknown", e);
            return null; // Never reached due to exception
        }
    }

    private void validateImageContentType(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }
    }

    private String buildFilePath(String category, String fileName) {
        return Paths.get(category, createDatePath(), generateRandomFilePrefix() + fileName)
                .toString()
                .replace('\\', '/');
    }

    private String createDatePath() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    private String generateRandomFilePrefix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, UUID_PREFIX_LENGTH);
    }

    private String getCloudFrontUrl(String fileKey) {
        return cloudFrontUrl + "/" + fileKey;
    }

    private void logAndThrowError(String category, String fileName, Exception e) {
        log.error("S3 파일 업로드 실패. category: {}, fileName: {}, error: {}",
                category, fileName, e.getMessage(), e);
        throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
    }
}