package com.montage.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.auditing.config.AuditingConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.montage.common.exception.GlobalExceptionHandler;


@SpringBootApplication
//@EnableDiscoveryClient
@ComponentScan(
    basePackages = {"com.montage.device", "com.montage.common"},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                GlobalExceptionHandler.class,
                AuditingConfiguration.class
            }
        )
    }
)
@EntityScan(basePackages = {"com.montage.device.entity", "com.montage.common.entity"})
@EnableJpaRepositories(basePackages = {"com.montage.device.repository", "com.montage.common.repository"})
public class DeviceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceServiceApplication.class, args);
    }
} 