package com.example.lettuce.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for database optimizations.
 * Provides recommendations for indexes and query optimizations based on application patterns.
 * 
 * This class documents the indexing strategy used throughout the application and provides
 * Hibernate property customization for better performance.
 */
@Configuration
@Slf4j
public class DatabaseOptimizationConfig {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:100}")
    private int batchSize;
    
    @Value("${spring.jpa.properties.hibernate.order_inserts:true}")
    private boolean orderInserts;
    
    @Value("${spring.jpa.properties.hibernate.order_updates:true}")
    private boolean orderUpdates;
    
    /**
     * Database indexing strategy used throughout the application:
     * 
     * 1. Primary Keys: All tables have numeric primary keys (BIGINT)
     * 
     * 2. Foreign Keys: All foreign keys are indexed to optimize joins
     * 
     * 3. Frequently Queried Fields:
     *    - User email (unique index)
     *    - CarbonFootprint.createdAt (range queries)
     *    - Aggregate IDs (UUID strings, must be indexed for Domain Event lookup)
     * 
     * 4. Partial/Function Indexes:
     *    - Active records: WHERE deleted_at IS NULL
     * 
     * 5. Composite Indexes:
     *    - (user_id, created_at) for time-based user queries
     *    - (product_category, carbon_value) for category analysis
     */
    
    /**
     * Customizes Hibernate properties for better database performance.
     * 
     * @return HibernatePropertiesCustomizer for optimizing ORM operations
     */
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            // Batch processing
            hibernateProperties.put("hibernate.jdbc.batch_size", batchSize);
            hibernateProperties.put("hibernate.order_inserts", orderInserts);
            hibernateProperties.put("hibernate.order_updates", orderUpdates);
            
            // Statement caching
            hibernateProperties.put("hibernate.jdbc.use_get_generated_keys", true);
            
            // Connection handling
            hibernateProperties.put("hibernate.connection.provider_disables_autocommit", true);
            
            // Statistics and metrics if enabled
            hibernateProperties.put("hibernate.generate_statistics", 
                    "${spring.jpa.properties.hibernate.generate_statistics:false}");
            
            log.info("Configured Hibernate optimizations: batch_size={}, order_inserts={}, order_updates={}", 
                    batchSize, orderInserts, orderUpdates);
        };
    }
} 