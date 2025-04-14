package com.logistics.load_booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.logistics.load_booking_service.model")
public class LoadBookingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoadBookingServiceApplication.class, args);
	}
}
