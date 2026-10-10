package com.rentease.controller;

import com.rentease.dto.ApiResponse;
import com.rentease.dto.BookingRequest;
import com.rentease.dto.BookingResponse;
import com.rentease.entity.User;
import com.rentease.service.AuthService;
import com.rentease.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService authService;

    public BookingController(BookingService bookingService, AuthService authService) {
        this.bookingService = bookingService;
        this.authService = authService;
    }

    @PostMapping
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        User currentUser = authService.getCurrentUser();
        BookingResponse booking = bookingService.createBooking(request, currentUser);
        return new ResponseEntity<>(ApiResponse.success("Rental request sent to property owner", booking), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getTenantBookings() {
        User currentUser = authService.getCurrentUser();
        List<BookingResponse> bookings = bookingService.getTenantBookings(currentUser);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getOwnerBookings() {
        User currentUser = authService.getCurrentUser();
        List<BookingResponse> bookings = bookingService.getOwnerBookings(currentUser);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        BookingResponse booking = bookingService.getBookingById(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> approveBooking(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        BookingResponse booking = bookingService.approveBooking(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Booking request approved", booking));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> rejectBooking(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        User currentUser = authService.getCurrentUser();
        String reason = payload != null ? payload.get("reason") : null;
        BookingResponse booking = bookingService.rejectBooking(id, currentUser, reason);
        return ResponseEntity.ok(ApiResponse.success("Booking request rejected", booking));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        BookingResponse booking = bookingService.cancelBooking(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
    }
}
