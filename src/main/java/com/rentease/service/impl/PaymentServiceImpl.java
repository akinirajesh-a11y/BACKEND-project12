package com.rentease.service.impl;

import com.rentease.dto.PaymentRequest;
import com.rentease.dto.PaymentResponse;
import com.rentease.entity.*;
import com.rentease.exception.BadRequestException;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.exception.UnauthorizedException;
import com.rentease.repository.BookingRepository;
import com.rentease.repository.PaymentRepository;
import com.rentease.service.PaymentService;
import com.rentease.service.RentalAgreementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final RentalAgreementService rentalAgreementService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingRepository bookingRepository,
                              RentalAgreementService rentalAgreementService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.rentalAgreementService = rentalAgreementService;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, User tenant) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (!booking.getTenant().getId().equals(tenant.getId())) {
            throw new UnauthorizedException("You are not authorized to make payment for this booking");
        }

        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("This booking has already been paid for");
        }

        if (booking.getBookingStatus() != BookingStatus.APPROVED) {
            throw new BadRequestException("Payment can only be completed for approved booking requests");
        }

        // Generate Simulated Transaction
        String transactionId = "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();

        Payment payment = new Payment();
        payment.setTransactionId(transactionId);
        payment.setBooking(booking);
        payment.setTenant(tenant);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setGatewayReference(request.getPaymentReference() != null && !request.getPaymentReference().isBlank()
                ? request.getPaymentReference()
                : "GATEWAY-SIM-" + System.currentTimeMillis());

        Payment savedPayment = paymentRepository.save(payment);

        // Update booking state
        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        booking.setBookingStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        // Automatically generate official legal rental agreement upon successful payment
        rentalAgreementService.generateAgreement(booking);

        return new PaymentResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBookingId(Long bookingId, User currentUser) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));

        if (!payment.getTenant().getId().equals(currentUser.getId()) &&
            !payment.getBooking().getOwner().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to view this payment");
        }

        return new PaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getTenantPayments(User tenant) {
        return paymentRepository.findByTenantIdOrderByPaymentDateDesc(tenant.getId()).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPaymentsForAdmin() {
        return paymentRepository.findAllByOrderByPaymentDateDesc().stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }
}
