package com.rentease.service.impl;

import com.rentease.dto.BookingRequest;
import com.rentease.dto.BookingResponse;
import com.rentease.entity.*;
import com.rentease.exception.BadRequestException;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.exception.UnauthorizedException;
import com.rentease.repository.BookingRepository;
import com.rentease.repository.PropertyRepository;
import com.rentease.service.BookingService;
import com.rentease.service.RentalAgreementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final RentalAgreementService rentalAgreementService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PropertyRepository propertyRepository,
                              RentalAgreementService rentalAgreementService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.rentalAgreementService = rentalAgreementService;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, User tenant) {
        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new BadRequestException("Rental end date must be after the start date");
        }

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + request.getPropertyId()));

        if (!property.isAvailable() || property.getStatus() != PropertyStatus.APPROVED) {
            throw new BadRequestException("This property is currently not available for rent");
        }

        if (property.getOwner().getId().equals(tenant.getId())) {
            throw new BadRequestException("Owners cannot rent their own properties");
        }

        // Check for conflicting confirmed bookings
        long overlaps = bookingRepository.countOverlappingBookings(
                property.getId(), request.getStartDate(), request.getEndDate());
        if (overlaps > 0) {
            throw new BadRequestException("This property already has an approved reservation for the selected date range");
        }

        Booking booking = new Booking();
        String bookingNumber = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        booking.setBookingNumber(bookingNumber);
        booking.setProperty(property);
        booking.setTenant(tenant);
        booking.setOwner(property.getOwner());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setMonthlyRent(property.getMonthlyRent());
        booking.setSecurityDeposit(property.getSecurityDeposit());
        booking.setTotalAmount(property.getMonthlyRent().add(property.getSecurityDeposit()));
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setSpecialRequests(request.getSpecialRequests());

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, User currentUser) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getTenant().getId().equals(currentUser.getId()) &&
            !booking.getOwner().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to view this booking");
        }

        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getTenantBookings(User tenant) {
        return bookingRepository.findByTenantIdOrderByCreatedAtDesc(tenant.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getOwnerBookings(User owner) {
        return bookingRepository.findByOwnerIdOrderByCreatedAtDesc(owner.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookingsForAdmin() {
        return bookingRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse approveBooking(Long id, User owner) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getOwner().getId().equals(owner.getId()) && owner.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to approve this booking");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be approved");
        }

        booking.setBookingStatus(BookingStatus.APPROVED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    @Override
    @Transactional
    public BookingResponse rejectBooking(Long id, User owner, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getOwner().getId().equals(owner.getId()) && owner.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to reject this booking");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be rejected");
        }

        booking.setBookingStatus(BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id, User tenant) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getTenant().getId().equals(tenant.getId()) && tenant.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to cancel this booking");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse(booking);
        response.setHasAgreement(rentalAgreementService.existsByBookingId(booking.getId()));
        return response;
    }
}
