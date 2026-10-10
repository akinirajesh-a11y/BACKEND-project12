package com.rentease.controller;

import com.rentease.dto.*;
import com.rentease.entity.User;
import com.rentease.service.AdminService;
import com.rentease.service.AuthService;
import com.rentease.service.BookingService;
import com.rentease.service.PaymentService;
import com.rentease.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final PropertyService propertyService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final AuthService authService;

    public AdminController(AdminService adminService,
                           PropertyService propertyService,
                           BookingService bookingService,
                           PaymentService paymentService,
                           AuthService authService) {
        this.adminService = adminService;
        this.propertyService = propertyService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.authService = authService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardStats>> getDashboardStats() {
        AdminDashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusUpdateRequest request
    ) {
        UserDTO user = adminService.updateUserStatus(id, request.getBlocked());
        String msg = request.getBlocked() ? "User has been blocked" : "User has been unblocked";
        return ResponseEntity.ok(ApiResponse.success(msg, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/properties")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperties() {
        List<PropertyResponse> properties = propertyService.getAllPropertiesForAdmin();
        return ResponseEntity.ok(ApiResponse.success(properties));
    }

    @PutMapping("/properties/{id}/approve")
    public ResponseEntity<ApiResponse<PropertyResponse>> approveProperty(@PathVariable Long id) {
        PropertyResponse response = propertyService.approveProperty(id);
        return ResponseEntity.ok(ApiResponse.success("Property listing approved", response));
    }

    @PutMapping("/properties/{id}/reject")
    public ResponseEntity<ApiResponse<PropertyResponse>> rejectProperty(@PathVariable Long id) {
        PropertyResponse response = propertyService.rejectProperty(id);
        return ResponseEntity.ok(ApiResponse.success("Property listing rejected", response));
    }

    @DeleteMapping("/properties/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProperty(@PathVariable Long id) {
        User admin = authService.getCurrentUser();
        propertyService.deleteProperty(id, admin);
        return ResponseEntity.ok(ApiResponse.success("Property listing removed", null));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookingsForAdmin();
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPaymentsForAdmin();
        return ResponseEntity.ok(ApiResponse.success(payments));
    }
}
