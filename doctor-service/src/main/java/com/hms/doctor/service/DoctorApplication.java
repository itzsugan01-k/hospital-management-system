package com.hms.doctor.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableKafka
@EnableScheduling
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class, args);

    }
}
