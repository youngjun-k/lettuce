package com.example.lettuce.global.shared.athena;

import com.amazonaws.services.athena.AmazonAthena;
import com.amazonaws.services.athena.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Service to query Parquet files using Amazon Athena.
 * This replaces S3 Select functionality which is being discontinued on October 31, 2025.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AthenaQueryService {

    private final AmazonAthena athenaClient;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.athena.database:carbon_footprint}")
    private String database;

    @Value("${cloud.aws.athena.output-location}")
    private String outputLocation;

    @Value("${cloud.aws.athena.query-timeout-ms:30000}")
    private long queryTimeoutMs;

    /**
     * Executes an Athena query against Parquet data in S3.
     * 
     * @param query The SQL query to execute
     * @return The query results as a String
     */
    public String executeQuery(String query) {
        try {
            // Generate a unique query execution ID
            String queryExecutionId = startQueryExecution(query);
            
            // Wait for the query to complete
            waitForQueryToComplete(queryExecutionId);
            
            // Get the query results
            return getQueryResults(queryExecutionId);
        } catch (Exception e) {
            log.error("Athena query execution failed. query: {}, error: {}", query, e.getMessage(), e);
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Starts the Athena query execution.
     */
    private String startQueryExecution(String query) {
        StartQueryExecutionRequest request = new StartQueryExecutionRequest()
                .withQueryString(query)
                .withQueryExecutionContext(new QueryExecutionContext().withDatabase(database))
                .withResultConfiguration(
                        new ResultConfiguration().withOutputLocation(outputLocation)
                );
                
        StartQueryExecutionResult result = athenaClient.startQueryExecution(request);
        return result.getQueryExecutionId();
    }
    
    /**
     * Waits for the query to complete, polling at regular intervals.
     */
    private void waitForQueryToComplete(String queryExecutionId) throws InterruptedException {
        GetQueryExecutionRequest request = new GetQueryExecutionRequest()
                .withQueryExecutionId(queryExecutionId);

        GetQueryExecutionResult result = null;
        boolean isQueryStillRunning = true;
        long startTime = System.currentTimeMillis();
        
        while (isQueryStillRunning) {
            result = athenaClient.getQueryExecution(request);
            String status = result.getQueryExecution().getStatus().getState();
            
            if (status.equals(QueryExecutionState.FAILED.toString())) {
                throw new RuntimeException("Athena query failed: " + 
                        result.getQueryExecution().getStatus().getStateChangeReason());
            } else if (status.equals(QueryExecutionState.CANCELLED.toString())) {
                throw new RuntimeException("Athena query was cancelled");
            } else if (status.equals(QueryExecutionState.SUCCEEDED.toString())) {
                isQueryStillRunning = false;
            } else {
                // Query is still running, wait before checking again
                Thread.sleep(1000);
                
                // Check if we've exceeded the timeout
                if (System.currentTimeMillis() - startTime > queryTimeoutMs) {
                    athenaClient.stopQueryExecution(new StopQueryExecutionRequest().withQueryExecutionId(queryExecutionId));
                    throw new RuntimeException("Athena query timed out after " + queryTimeoutMs + "ms");
                }
            }
        }
    }
    
    /**
     * Gets the query results from S3.
     */
    private String getQueryResults(String queryExecutionId) throws IOException {
        // Athena stores results in the S3 output location
        // The path follows the pattern: s3://bucket/prefix/queryID.csv
        String resultKey = getResultObjectKey(queryExecutionId);
        String bucket = outputLocation.replace("s3://", "").split("/")[0];
        
        S3Object object = amazonS3.getObject(bucket, resultKey);
        
        StringBuilder resultBuilder = new StringBuilder();
        try (S3ObjectInputStream inputStream = object.getObjectContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line).append("\n");
            }
        }
        
        return resultBuilder.toString();
    }
    
    /**
     * Extracts the S3 key for the query results.
     */
    private String getResultObjectKey(String queryExecutionId) {
        // Parse the output location to get the result key
        // Output location format: s3://bucket/prefix/
        String prefix = outputLocation.replace("s3://", "").split("/", 2)[1];
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }
        
        return prefix + queryExecutionId + ".csv";
    }
} 