package com.logistics.load_booking_service.dto;

import com.logistics.load_booking_service.model.BookingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingDTO {
    @NotNull(message = "Load ID is required")
    private UUID loadId;

    @NotBlank(message = "Transporter ID is required")
    private String transporterId;

    @Min(value = 0, message = "Proposed rate cannot be negative")
    private double proposedRate;

    private String comment;
    private BookingStatus status;
    private LocalDateTime requestedAt;
    private UUID id;
}