package com.example.lettuce.global.config;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Configuration class for Hadoop and Parquet.
 * Sets up the Hadoop configuration for Parquet file operations.
 */
@org.springframework.context.annotation.Configuration
public class HadoopConfig {

    @Value("${app.storage.parquet.path:/tmp/parquet-data}")
    private String parquetBasePath;
    
    /**
     * Creates a Hadoop Configuration with appropriate settings for Parquet.
     * 
     * @return The Hadoop Configuration
     */
    @Bean
    public Configuration hadoopConfiguration() {
        Configuration conf = new Configuration();
        
        // Set Hadoop filesystem implementation to local filesystem
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        
        // Set the base path for Parquet files
        conf.set("parquet.base.path", parquetBasePath);
        
        // Configure Parquet compression
        conf.set("parquet.compression", "SNAPPY");
        
        // Configure Parquet block size (1MB)
        conf.setInt("parquet.block.size", 1024 * 1024);
        
        // Configure Parquet page size (8KB)
        conf.setInt("parquet.page.size", 8 * 1024);
        
        return conf;
    }
} 