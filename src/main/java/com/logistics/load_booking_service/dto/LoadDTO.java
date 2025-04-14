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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Shipper ID is required") String getShipperId() {
        return shipperId;
    }

    public void setShipperId(@NotBlank(message = "Shipper ID is required") String shipperId) {
        this.shipperId = shipperId;
    }

    public @NotNull(message = "Facility details are required") @Valid FacilityDTO getFacility() {
        return facility;
    }

    public void setFacility(@NotNull(message = "Facility details are required") @Valid FacilityDTO facility) {
        this.facility = facility;
    }

    public @NotBlank(message = "Product type is required") String getProductType() {
        return productType;
    }

    public void setProductType(@NotBlank(message = "Product type is required") String productType) {
        this.productType = productType;
    }

    public @NotBlank(message = "Truck type is required") String getTruckType() {
        return truckType;
    }

    public void setTruckType(@NotBlank(message = "Truck type is required") String truckType) {
        this.truckType = truckType;
    }

    @Min(value = 1, message = "Number of trucks must be at least 1")
    public int getNoOfTrucks() {
        return noOfTrucks;
    }

    public void setNoOfTrucks(@Min(value = 1, message = "Number of trucks must be at least 1") int noOfTrucks) {
        this.noOfTrucks = noOfTrucks;
    }

    @Min(value = 0, message = "Weight cannot be negative")
    public double getWeight() {
        return weight;
    }

    public void setWeight(@Min(value = 0, message = "Weight cannot be negative") double weight) {
        this.weight = weight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LoadStatus getStatus() {
        return status;
    }

    public void setStatus(LoadStatus status) {
        this.status = status;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }
}