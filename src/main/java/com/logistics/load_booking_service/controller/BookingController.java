package com.logistics.load_booking_service.controller;

import com.logistics.load_booking_service.dto.BookingDTO;
import com.logistics.load_booking_service.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings(
            @RequestParam(required = false) String transporterId,
            @RequestParam(required = false) UUID loadId) {

        List<BookingDTO> bookings;

        if (transporterId != null) {
            bookings = bookingService.getBookingsByTransporterId(transporterId);
        } else if (loadId != null) {
            bookings = bookingService.getBookingsByLoadId(loadId);
        } else {
            bookings = bookingService.getAllBookings();
        }

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable UUID bookingId) {
        BookingDTO booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable UUID bookingId,
            @Valid @RequestBody BookingDTO bookingDTO) {

        BookingDTO updatedBooking = bookingService.updateBooking(bookingId, bookingDTO);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}