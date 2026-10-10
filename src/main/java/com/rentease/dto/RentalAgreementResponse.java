package com.rentease.dto;

import com.rentease.entity.RentalAgreement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentalAgreementResponse {

    private Long id;
    private String agreementNumber;
    private Long bookingId;
    private String bookingNumber;
    private UserDTO tenant;
    private UserDTO owner;
    private PropertyResponse property;
    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate agreementDate;
    private String termsAndConditions;
    private String status;
    private LocalDateTime createdAt;

    public RentalAgreementResponse() {
    }

    public RentalAgreementResponse(RentalAgreement agreement) {
        if (agreement != null) {
            this.id = agreement.getId();
            this.agreementNumber = agreement.getAgreementNumber();
            if (agreement.getBooking() != null) {
                this.bookingId = agreement.getBooking().getId();
                this.bookingNumber = agreement.getBooking().getBookingNumber();
            }
            if (agreement.getTenant() != null) {
                this.tenant = new UserDTO(agreement.getTenant());
            }
            if (agreement.getOwner() != null) {
                this.owner = new UserDTO(agreement.getOwner());
            }
            if (agreement.getProperty() != null) {
                this.property = new PropertyResponse(agreement.getProperty());
            }
            this.monthlyRent = agreement.getMonthlyRent();
            this.securityDeposit = agreement.getSecurityDeposit();
            this.startDate = agreement.getStartDate();
            this.endDate = agreement.getEndDate();
            this.agreementDate = agreement.getAgreementDate();
            this.termsAndConditions = agreement.getTermsAndConditions();
            this.status = agreement.getStatus();
            this.createdAt = agreement.getCreatedAt();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
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

    public PropertyResponse getProperty() {
        return property;
    }

    public void setProperty(PropertyResponse property) {
        this.property = property;
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

    public LocalDate getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(LocalDate agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
