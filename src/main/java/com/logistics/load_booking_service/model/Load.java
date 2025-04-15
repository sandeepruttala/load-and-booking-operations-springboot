package com.logistics.load_booking_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "loads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Load {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String shipperId;

    @Embedded
    private Facility facility;

    private String productType;
    private String truckType;
    private int noOfTrucks;
    private double weight;
    private String comment;

    @Column(updatable = false)
    private LocalDateTime datePosted;

    @Enumerated(EnumType.STRING)
    private LoadStatus status;

    @PrePersist
    protected void onCreate() {
        datePosted = LocalDateTime.now();
        if (status == null) {
            status = LoadStatus.POSTED;
        }
    }

}
