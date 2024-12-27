package com.montage.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class HikariConfig {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(databaseUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        // HikariCP settings - these are the most commonly used
        dataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1);
        dataSource.setMinimumIdle(10);
        dataSource.setIdleTimeout(300000); // 5 minutes
        dataSource.setConnectionTimeout(20000); // 20 seconds
        dataSource.setMaxLifetime(1200000); // 20 minutes
        dataSource.setPoolName("HikariPool-Main");
        dataSource.setLeakDetectionThreshold(60000); // 1 minute
        
        // Enable JMX monitoring
        dataSource.setRegisterMbeans(true);
        
        return dataSource;
    }
} 