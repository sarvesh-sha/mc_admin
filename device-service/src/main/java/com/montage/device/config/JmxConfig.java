package com.montage.device.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.export.MBeanExporter;

@Configuration
public class JmxConfig {
    
    @Bean
    public MBeanExporter mBeanExporter() {
        MBeanExporter exporter = new MBeanExporter();
        exporter.setAutodetect(false);
        exporter.setExcludedBeans("dataSource");
        return exporter;
    }

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean factoryBean = new MBeanServerFactoryBean();
        factoryBean.setLocateExistingServerIfPossible(true);
        return factoryBean;
    }
} 