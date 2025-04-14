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
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotNull(message = "Load ID is required") UUID getLoadId() {
        return loadId;
    }

    public void setLoadId(@NotNull(message = "Load ID is required") UUID loadId) {
        this.loadId = loadId;
    }

    public @NotBlank(message = "Transporter ID is required") String getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(@NotBlank(message = "Transporter ID is required") String transporterId) {
        this.transporterId = transporterId;
    }

    @Min(value = 0, message = "Proposed rate cannot be negative")
    public double getProposedRate() {
        return proposedRate;
    }

    public void setProposedRate(@Min(value = 0, message = "Proposed rate cannot be negative") double proposedRate) {
        this.proposedRate = proposedRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    private UUID id;

    @NotNull(message = "Load ID is required")
    private UUID loadId;

    @NotBlank(message = "Transporter ID is required")
    private String transporterId;

    @Min(value = 0, message = "Proposed rate cannot be negative")
    private double proposedRate;

    private String comment;
    private BookingStatus status;
    private LocalDateTime requestedAt;
}