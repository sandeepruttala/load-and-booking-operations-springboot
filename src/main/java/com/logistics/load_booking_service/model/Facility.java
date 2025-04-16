package com.logistics.load_booking_service.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Facility {
    private String loadingPoint;
    private String unloadingPoint;
    private LocalDateTime loadingDate;
    private LocalDateTime unloadingDate;
}