package com.rentease.controller;

import com.rentease.dto.ApiResponse;
import com.rentease.dto.PaymentRequest;
import com.rentease.dto.PaymentResponse;
import com.rentease.entity.User;
import com.rentease.service.AuthService;
import com.rentease.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthService authService;

    public PaymentController(PaymentService paymentService, AuthService authService) {
        this.paymentService = paymentService;
        this.authService = authService;
    }

    @PostMapping
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@Valid @RequestBody PaymentRequest request) {
        User currentUser = authService.getCurrentUser();
        PaymentResponse payment = paymentService.processPayment(request, currentUser);
        return new ResponseEntity<>(ApiResponse.success("Payment completed successfully! Rental agreement generated.", payment), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments() {
        User currentUser = authService.getCurrentUser();
        List<PaymentResponse> payments = paymentService.getTenantPayments(currentUser);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByBookingId(@PathVariable Long bookingId) {
        User currentUser = authService.getCurrentUser();
        PaymentResponse payment = paymentService.getPaymentByBookingId(bookingId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }
}
