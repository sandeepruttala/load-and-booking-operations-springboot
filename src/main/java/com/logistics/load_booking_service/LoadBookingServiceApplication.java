package com.logistics.load_booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EntityScan(basePackages = "com.logistics.load_booking_service.model")
public class LoadBookingServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoadBookingServiceApplication.class);

    public static void main(String[] args) {
        logger.info("Starting LoadBookingServiceApplication...");
        SpringApplication.run(LoadBookingServiceApplication.class, args);
        logger.info("LoadBookingServiceApplication started successfully.");
    }
}
