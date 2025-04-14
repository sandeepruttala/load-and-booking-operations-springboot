package com.logistics.load_booking_service.repository;

import com.logistics.load_booking_service.model.Load;
import com.logistics.load_booking_service.model.LoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoadRepository extends JpaRepository<Load, UUID> {
    List<Load> findByShipperId(String shipperId);
    List<Load> findByTruckType(String truckType);
    List<Load> findByStatus(LoadStatus status);
}