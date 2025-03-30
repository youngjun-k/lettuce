package com.example.lettuce.global.shared.s3;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.SelectObjectContentRequest;
import com.amazonaws.services.s3.model.SelectObjectContentResult;
import com.amazonaws.services.s3.model.InputSerialization;
import com.amazonaws.services.s3.model.JSONOutput;
import com.amazonaws.services.s3.model.OutputSerialization;
import com.amazonaws.services.s3.model.ParquetInput;
import com.amazonaws.services.s3.model.ExpressionType;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated(since = "2024-10-31", forRemoval = true)
public class S3Selector {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * Use S3 Select to query Parquet files stored in S3
     * 
     * @param filePath The path to the Parquet file in S3
     * @param query    The SQL query to execute against the Parquet file
     * @return The query results as a String
     */
    public String selectParquetFileFromS3(String filePath, String query) {
        try {
            // Configure the input serialization to use Parquet format
            InputSerialization inputSerialization = new InputSerialization()
                    .withParquet(new ParquetInput());

            // Configure the output serialization to CSV format
            OutputSerialization outputSerialization = new OutputSerialization()
                    .withJson(new JSONOutput());

            // Create the select request
            SelectObjectContentRequest request = new SelectObjectContentRequest()
                    .withBucketName(bucket)
                    .withKey(filePath)
                    .withExpression(query)
                    .withExpressionType(ExpressionType.SQL)
                    .withInputSerialization(inputSerialization)
                    .withOutputSerialization(outputSerialization);

            // Execute the query
            SelectObjectContentResult result = amazonS3.selectObjectContent(request);

            // Process the result
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            AtomicBoolean isResultComplete = new AtomicBoolean(false);

            // Process the response stream
            InputStream resultInputStream = result.getPayload().getRecordsInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read the results
            while ((bytesRead = resultInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            resultInputStream.close();
            outputStream.close();

            return outputStream.toString();

        } catch (Exception e) {
            if (e.getMessage().contains("MethodNotAllowed") || e.getMessage().contains("405")) {
                log.error("S3 Select operation not allowed. This may be due to insufficient IAM permissions or unsupported S3 endpoint. " +
                        "Make sure your IAM policy includes 's3:SelectObjectContent' permission. " +
                        "filePath: {}, query: {}, error: {}", filePath, query, e.getMessage(), e);
            } else {
                log.error("S3 Select operation failed. filePath: {}, query: {}, error: {}",
                        filePath, query, e.getMessage(), e);
            }
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
