package com.prunny.project_service.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.prunny.project_service.services.clients")
public class CustomFeignClient {
}
