package com.example.lettuce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.lettuce.domain",
    "com.example.lettuce.application",
    "com.example.lettuce.port",
    "com.example.lettuce.config",
    "com.example.lettuce.api",
    "com.example.lettuce.global"
})
public class LettuceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LettuceApplication.class, args);
	}

}
