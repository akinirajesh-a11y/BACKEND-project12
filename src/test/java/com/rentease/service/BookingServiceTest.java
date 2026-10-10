package com.rentease.service;

import com.rentease.dto.BookingRequest;
import com.rentease.dto.BookingResponse;
import com.rentease.entity.*;
import com.rentease.repository.BookingRepository;
import com.rentease.repository.PropertyRepository;
import com.rentease.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private RentalAgreementService rentalAgreementService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User tenant;
    private Property property;
    private Booking booking;

    @BeforeEach
    void setUp() {
        owner = new User("Owner Name", "owner@example.com", "pass", "1234567890", Role.ROLE_OWNER);
        owner.setId(10L);

        tenant = new User("Tenant Name", "tenant@example.com", "pass", "9876543210", Role.ROLE_TENANT);
        tenant.setId(20L);

        property = new Property();
        property.setId(100L);
        property.setTitle("Luxury 2BHK");
        property.setMonthlyRent(new BigDecimal("35000.00"));
        property.setSecurityDeposit(new BigDecimal("70000.00"));
        property.setStatus(PropertyStatus.APPROVED);
        property.setAvailable(true);
        property.setOwner(owner);

        booking = new Booking();
        booking.setId(500L);
        booking.setBookingNumber("BK-TEST01");
        booking.setProperty(property);
        booking.setTenant(tenant);
        booking.setOwner(owner);
        booking.setStartDate(LocalDate.now().plusDays(2));
        booking.setEndDate(LocalDate.now().plusDays(365));
        booking.setMonthlyRent(property.getMonthlyRent());
        booking.setSecurityDeposit(property.getSecurityDeposit());
        booking.setTotalAmount(new BigDecimal("105000.00"));
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
    }

    @Test
    void testCreateBooking_Success() {
        BookingRequest request = new BookingRequest(100L, LocalDate.now().plusDays(2), LocalDate.now().plusDays(365), "Quiet area please");

        when(propertyRepository.findById(100L)).thenReturn(Optional.of(property));
        when(bookingRepository.countOverlappingBookings(eq(100L), any(), any())).thenReturn(0L);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(rentalAgreementService.existsByBookingId(any())).thenReturn(false);

        BookingResponse response = bookingService.createBooking(request, tenant);

        assertNotNull(response);
        assertEquals(500L, response.getId());
        assertEquals("BK-TEST01", response.getBookingNumber());
        assertEquals(BookingStatus.PENDING, response.getBookingStatus());
    }

    @Test
    void testApproveBooking_Success() {
        when(bookingRepository.findById(500L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(rentalAgreementService.existsByBookingId(500L)).thenReturn(false);

        BookingResponse response = bookingService.approveBooking(500L, owner);

        assertNotNull(response);
        assertEquals(BookingStatus.APPROVED, booking.getBookingStatus());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testCancelBooking_Success() {
        when(bookingRepository.findById(500L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(rentalAgreementService.existsByBookingId(500L)).thenReturn(false);

        BookingResponse response = bookingService.cancelBooking(500L, tenant);

        assertNotNull(response);
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    }
}
