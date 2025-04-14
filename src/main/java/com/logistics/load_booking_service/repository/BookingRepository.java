package com.logistics.load_booking_service.repository;

import com.logistics.load_booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByTransporterId(String transporterId);
    List<Booking> findByLoadId(UUID loadId);
    Optional<Booking> findByLoadIdAndTransporterId(UUID loadId, String transporterId);
}