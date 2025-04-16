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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        logger.info("Entering createBooking with bookingDTO: {}", bookingDTO);
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        logger.info("Exiting createBooking with createdBooking: {}", createdBooking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings(
            @RequestParam(required = false) String transporterId,
            @RequestParam(required = false) UUID loadId) {
        logger.info("Entering getBookings with transporterId: {}, loadId: {}", transporterId, loadId);

        List<BookingDTO> bookings;

        if (transporterId != null) {
            bookings = bookingService.getBookingsByTransporterId(transporterId);
        } else if (loadId != null) {
            bookings = bookingService.getBookingsByLoadId(loadId);
        } else {
            bookings = bookingService.getAllBookings();
        }

        logger.info("Exiting getBookings with {} bookings", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable UUID bookingId) {
        logger.info("Entering getBookingById with bookingId: {}", bookingId);
        BookingDTO booking = bookingService.getBookingById(bookingId);
        logger.info("Exiting getBookingById with booking: {}", booking);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable UUID bookingId,
            @Valid @RequestBody BookingDTO bookingDTO) {
        logger.info("Entering updateBooking with bookingId: {}, bookingDTO: {}", bookingId, bookingDTO);
        BookingDTO updatedBooking = bookingService.updateBooking(bookingId, bookingDTO);
        logger.info("Exiting updateBooking with updatedBooking: {}", updatedBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        logger.info("Entering deleteBooking with bookingId: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        logger.info("Exiting deleteBooking with bookingId: {}", bookingId);
        return ResponseEntity.noContent().build();
    }
}
