package com.rentease.dto;

import com.rentease.entity.Payment;
import com.rentease.entity.PaymentMethod;
import com.rentease.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private String transactionId;
    private Long bookingId;
    private String bookingNumber;
    private String propertyTitle;
    private String tenantName;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private String gatewayReference;

    public PaymentResponse() {
    }

    public PaymentResponse(Payment payment) {
        if (payment != null) {
            this.id = payment.getId();
            this.transactionId = payment.getTransactionId();
            if (payment.getBooking() != null) {
                this.bookingId = payment.getBooking().getId();
                this.bookingNumber = payment.getBooking().getBookingNumber();
                if (payment.getBooking().getProperty() != null) {
                    this.propertyTitle = payment.getBooking().getProperty().getTitle();
                }
            }
            if (payment.getTenant() != null) {
                this.tenantName = payment.getTenant().getName();
            }
            this.amount = payment.getAmount();
            this.paymentMethod = payment.getPaymentMethod();
            this.paymentStatus = payment.getPaymentStatus();
            this.paymentDate = payment.getPaymentDate();
            this.gatewayReference = payment.getGatewayReference();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getGatewayReference() {
        return gatewayReference;
    }

    public void setGatewayReference(String gatewayReference) {
        this.gatewayReference = gatewayReference;
    }
}
