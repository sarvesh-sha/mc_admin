package com.montage.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.auditing.config.AuditingConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.montage.common.exception.GlobalExceptionHandler;


@SpringBootApplication(scanBasePackages = "com.montage.device")
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
@EnableJpaRepositories(basePackages = {"com.montage.device.repository", "com.montage.common.repository"},
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class DeviceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceServiceApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins(
                        "http://localhost:4200",
                        "http://74.179.58.105:8080",
                        "http://localhost:3000"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
} 