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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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
        logger.info("Entering createBooking with bookingDTO: {}", bookingDTO);
        Load load = loadRepository.findById(bookingDTO.getLoadId())
                .orElseThrow(() -> {
                    logger.error("Load not found with id: {}", bookingDTO.getLoadId());
                    return new ResourceNotFoundException("Load not found with id: " + bookingDTO.getLoadId());
                });

        if (load.getStatus() == LoadStatus.CANCELLED) {
            logger.error("Cannot book a cancelled load with id: {}", bookingDTO.getLoadId());
            throw new BusinessLogicException("Cannot book a cancelled load");
        }

        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDTO, booking);
        booking.setStatus(BookingStatus.PENDING);
        Booking savedBooking = bookingRepository.save(booking);

        loadService.updateLoadStatus(bookingDTO.getLoadId(), LoadStatus.BOOKED);

        BookingDTO savedBookingDTO = new BookingDTO();
        BeanUtils.copyProperties(savedBooking, savedBookingDTO);
        logger.info("Exiting createBooking with savedBookingDTO: {}", savedBookingDTO);
        return savedBookingDTO;
    }

    public List<BookingDTO> getAllBookings() {
        logger.info("Entering getAllBookings");
        List<BookingDTO> bookings = bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Exiting getAllBookings with {} bookings", bookings.size());
        return bookings;
    }

    public List<BookingDTO> getBookingsByTransporterId(String transporterId) {
        logger.info("Entering getBookingsByTransporterId with transporterId: {}", transporterId);
        List<BookingDTO> bookings = bookingRepository.findByTransporterId(transporterId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Exiting getBookingsByTransporterId with {} bookings", bookings.size());
        return bookings;
    }

    public List<BookingDTO> getBookingsByLoadId(UUID loadId) {
        logger.info("Entering getBookingsByLoadId with loadId: {}", loadId);
        List<BookingDTO> bookings = bookingRepository.findByLoadId(loadId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Exiting getBookingsByLoadId with {} bookings", bookings.size());
        return bookings;
    }

    public BookingDTO getBookingById(UUID bookingId) {
        logger.info("Entering getBookingById with bookingId: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found with id: " + bookingId);
                });
        BookingDTO bookingDTO = convertToDTO(booking);
        logger.info("Exiting getBookingById with bookingDTO: {}", bookingDTO);
        return bookingDTO;
    }

    @Transactional
    public BookingDTO updateBooking(UUID bookingId, BookingDTO bookingDTO) {
        logger.info("Entering updateBooking with bookingId: {}, bookingDTO: {}", bookingId, bookingDTO);
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found with id: " + bookingId);
                });

        bookingDTO.setId(existingBooking.getId());
        bookingDTO.setLoadId(existingBooking.getLoadId());
        bookingDTO.setTransporterId(existingBooking.getTransporterId());
        bookingDTO.setRequestedAt(existingBooking.getRequestedAt());

        if (bookingDTO.getStatus() == BookingStatus.ACCEPTED && existingBooking.getStatus() != BookingStatus.ACCEPTED) {
            bookingRepository.findByLoadId(existingBooking.getLoadId()).stream()
                    .filter(b -> !b.getId().equals(bookingId))
                    .forEach(b -> {
                        b.setStatus(BookingStatus.REJECTED);
                        bookingRepository.save(b);
                    });
        }

        BeanUtils.copyProperties(bookingDTO, existingBooking);
        Booking updatedBooking = bookingRepository.save(existingBooking);
        BookingDTO updatedBookingDTO = convertToDTO(updatedBooking);
        logger.info("Exiting updateBooking with updatedBookingDTO: {}", updatedBookingDTO);
        return updatedBookingDTO;
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        logger.info("Entering deleteBooking with bookingId: {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found with id: " + bookingId);
                });

        loadService.updateLoadStatus(booking.getLoadId(), LoadStatus.CANCELLED);

        bookingRepository.deleteById(bookingId);
        logger.info("Exiting deleteBooking with bookingId: {}", bookingId);
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        BeanUtils.copyProperties(booking, bookingDTO);
        return bookingDTO;
    }
}
