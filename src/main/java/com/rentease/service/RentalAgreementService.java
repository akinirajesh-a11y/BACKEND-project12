package com.rentease.service;

import com.rentease.dto.RentalAgreementResponse;
import com.rentease.entity.Booking;
import com.rentease.entity.RentalAgreement;
import com.rentease.entity.User;

import java.util.List;

public interface RentalAgreementService {
    RentalAgreement generateAgreement(Booking booking);
    RentalAgreementResponse getAgreementByBookingId(Long bookingId, User currentUser);
    List<RentalAgreementResponse> getTenantAgreements(User tenant);
    List<RentalAgreementResponse> getOwnerAgreements(User owner);
    boolean existsByBookingId(Long bookingId);
}
