package com.example.lettuce.global.shared.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.global.shared.athena.AthenaQueryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Uploader s3Uploader;
    private final S3Selector s3Selector;
    private final AthenaQueryService athenaQueryService;

    public static final String CATEGORY_MEMBER = "user";
    public static final String CATEGORY_CARBON_FOOTPRINT = "carbon-footprint";
    public static final String CATEGORY_PARQUET = "parquet";

    public UploadImageInfo uploadMemberProfileImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_MEMBER, image);
    }

    public UploadImageInfo uploadCarbonFootprintImage(byte[] imageContent, String filename, String contentType) {
        return s3Uploader.uploadBytesToBucket(CATEGORY_CARBON_FOOTPRINT, imageContent, filename, contentType);
    }

    public void uploadParquetFileToS3(String filename, File file) {
        s3Uploader.uploadParquetFileToS3(CATEGORY_PARQUET, filename, file);
    }

    /**
     * @deprecated S3 Select is being discontinued on October 31, 2025.
     *             Use {@link #queryParquetFileWithAthena(String, String)} instead.
     */
    @Deprecated(since = "2024-10-31", forRemoval = true)
    public String getParquetFileFromS3(String filePath, String query) {
        log.warn(
                "Using deprecated S3 Select which will be discontinued on October 31, 2024. Consider migrating to Athena.");
        return s3Selector.selectParquetFileFromS3(filePath, query);
    }

    /**
     * Queries Parquet files stored in S3 using Amazon Athena.
     * This is the recommended replacement for S3 Select which is being
     * discontinued.
     * 
     * @param filePath The path to the Parquet file (used to build the Athena table
     *                 name)
     * @param sqlQuery The SQL query to execute
     * @return The query results as a String
     */
    public String queryParquetFileWithAthena(String sqlQuery) {
        // If the query is "SELECT * FROM S3Object", convert it to Athena format
        // This handles the migration from S3 Select to Athena
        if (sqlQuery.equalsIgnoreCase("SELECT * FROM S3Object")) {
            // For Athena, we need to use the actual table name
            // We'll extract a simplified table name from the file path
            sqlQuery = "SELECT * FROM " + "carbon_footprint";
        }

        return athenaQueryService.executeQuery(sqlQuery);
    }

}
