package com.logistics.load_booking_service.dto;

import com.logistics.load_booking_service.model.LoadStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoadDTO {
    private UUID id;

    @NotBlank(message = "Shipper ID is required")
    private String shipperId;

    @NotNull(message = "Facility details are required")
    @Valid
    private FacilityDTO facility;

    @NotBlank(message = "Product type is required")
    private String productType;

    @NotBlank(message = "Truck type is required")
    private String truckType;

    @Min(value = 1, message = "Number of trucks must be at least 1")
    private int noOfTrucks;

    @Min(value = 0, message = "Weight cannot be negative")
    private double weight;

    private String comment;
    private LoadStatus status;
    private LocalDateTime datePosted;
}