package com.example.lettuce.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for Drools.
 * Sets up the Drools rule engine and loads rule files.
 */
@Slf4j
@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/";

    /**
     * Creates a KieContainer that holds all the Drools rules.
     * 
     * @return The KieContainer
     * @throws Exception If there's an error loading the rules
     */
    @Bean
    public KieContainer kieContainer() throws Exception {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        // Load all rule files from the rules directory
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
        
        for (Resource resource : resources) {
            String path = RULES_PATH + resource.getFilename();
            log.info("Loading Drools rule file: {}", path);
            kieFileSystem.write(ResourceFactory.newClassPathResource(path, getClass()));
        }
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        KieModule kieModule = kieBuilder.getKieModule();
        
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
} 