package com.logistics.load_booking_service.service;

import com.logistics.load_booking_service.dto.BookingDTO;
import com.logistics.load_booking_service.exception.BusinessLogicException;
import com.logistics.load_booking_service.exception.ResourceNotFoundException;
import com.logistics.load_booking_service.model.Booking;
import com.logistics.load_booking_service.model.BookingStatus;
import com.logistics.load_booking_service.model.Load;
import com.logistics.load_booking_service.model.LoadStatus;
import com.logistics.load_booking_service.repository.BookingRepository;
import com.logistics.load_booking_service.repository.LoadRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final LoadRepository loadRepository;
    private final LoadService loadService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, LoadRepository loadRepository, LoadService loadService) {
        this.bookingRepository = bookingRepository;
        this.loadRepository = loadRepository;
        this.loadService = loadService;
    }

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // Check if load exists and is not cancelled
        Load load = loadRepository.findById(bookingDTO.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + bookingDTO.getLoadId()));

        if (load.getStatus() == LoadStatus.CANCELLED) {
            throw new BusinessLogicException("Cannot book a cancelled load");
        }

        // Create booking
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDTO, booking);
        booking.setStatus(BookingStatus.PENDING);
        Booking savedBooking = bookingRepository.save(booking);

        // Update load status to BOOKED
        loadService.updateLoadStatus(bookingDTO.getLoadId(), LoadStatus.BOOKED);

        BookingDTO savedBookingDTO = new BookingDTO();
        BeanUtils.copyProperties(savedBooking, savedBookingDTO);
        return savedBookingDTO;
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByTransporterId(String transporterId) {
        return bookingRepository.findByTransporterId(transporterId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByLoadId(UUID loadId) {
        return bookingRepository.findByLoadId(loadId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return convertToDTO(booking);
    }

    @Transactional
    public BookingDTO updateBooking(UUID bookingId, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        // Don't update id, loadId, transporterId, and requestedAt
        bookingDTO.setId(existingBooking.getId());
        bookingDTO.setLoadId(existingBooking.getLoadId());
        bookingDTO.setTransporterId(existingBooking.getTransporterId());
        bookingDTO.setRequestedAt(existingBooking.getRequestedAt());

        // Handle status updates
        if (bookingDTO.getStatus() == BookingStatus.ACCEPTED && existingBooking.getStatus() != BookingStatus.ACCEPTED) {
            // Update other bookings for this load to REJECTED
            bookingRepository.findByLoadId(existingBooking.getLoadId()).stream()
                    .filter(b -> !b.getId().equals(bookingId))
                    .forEach(b -> {
                        b.setStatus(BookingStatus.REJECTED);
                        bookingRepository.save(b);
                    });
        }

        BeanUtils.copyProperties(bookingDTO, existingBooking);
        Booking updatedBooking = bookingRepository.save(existingBooking);

        return convertToDTO(updatedBooking);
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        // Update load status to CANCELLED when booking is deleted
        loadService.updateLoadStatus(booking.getLoadId(), LoadStatus.CANCELLED);

        bookingRepository.deleteById(bookingId);
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        BeanUtils.copyProperties(booking, bookingDTO);
        return bookingDTO;
    }
}