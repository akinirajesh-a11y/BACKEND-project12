package com.rentease.service;

import com.rentease.dto.BookingRequest;
import com.rentease.dto.BookingResponse;
import com.rentease.entity.User;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, User tenant);
    BookingResponse getBookingById(Long id, User currentUser);
    List<BookingResponse> getTenantBookings(User tenant);
    List<BookingResponse> getOwnerBookings(User owner);
    List<BookingResponse> getAllBookingsForAdmin();
    BookingResponse approveBooking(Long id, User owner);
    BookingResponse rejectBooking(Long id, User owner, String reason);
    BookingResponse cancelBooking(Long id, User tenant);
}
