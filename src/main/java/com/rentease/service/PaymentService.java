package com.rentease.service;

import com.rentease.dto.PaymentRequest;
import com.rentease.dto.PaymentResponse;
import com.rentease.entity.User;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request, User tenant);
    PaymentResponse getPaymentByBookingId(Long bookingId, User currentUser);
    List<PaymentResponse> getTenantPayments(User tenant);
    List<PaymentResponse> getAllPaymentsForAdmin();
}
