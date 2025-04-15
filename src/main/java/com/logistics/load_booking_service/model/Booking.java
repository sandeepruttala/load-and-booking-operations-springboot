package com.logistics.load_booking_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private UUID loadId;
    private String transporterId;
    private double proposedRate;
    private String comment;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(updatable = false)
    private LocalDateTime requestedAt;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }
}