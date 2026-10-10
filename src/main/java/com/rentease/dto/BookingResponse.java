package com.rentease.dto;

import com.rentease.entity.Booking;
import com.rentease.entity.BookingStatus;
import com.rentease.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private String bookingNumber;
    private PropertyResponse property;
    private UserDTO tenant;
    private UserDTO owner;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private BigDecimal totalAmount;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private String specialRequests;
    private boolean hasAgreement;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    public BookingResponse(Booking booking) {
        if (booking != null) {
            this.id = booking.getId();
            this.bookingNumber = booking.getBookingNumber();
            if (booking.getProperty() != null) {
                this.property = new PropertyResponse(booking.getProperty());
            }
            if (booking.getTenant() != null) {
                this.tenant = new UserDTO(booking.getTenant());
            }
            if (booking.getOwner() != null) {
                this.owner = new UserDTO(booking.getOwner());
            }
            this.startDate = booking.getStartDate();
            this.endDate = booking.getEndDate();
            this.monthlyRent = booking.getMonthlyRent();
            this.securityDeposit = booking.getSecurityDeposit();
            this.totalAmount = booking.getTotalAmount();
            this.bookingStatus = booking.getBookingStatus();
            this.paymentStatus = booking.getPaymentStatus();
            this.specialRequests = booking.getSpecialRequests();
            this.createdAt = booking.getCreatedAt();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public PropertyResponse getProperty() {
        return property;
    }

    public void setProperty(PropertyResponse property) {
        this.property = property;
    }

    public UserDTO getTenant() {
        return tenant;
    }

    public void setTenant(UserDTO tenant) {
        this.tenant = tenant;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public boolean isHasAgreement() {
        return hasAgreement;
    }

    public void setHasAgreement(boolean hasAgreement) {
        this.hasAgreement = hasAgreement;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
