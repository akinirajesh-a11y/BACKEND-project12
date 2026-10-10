package com.rentease.service.impl;

import com.rentease.dto.RentalAgreementResponse;
import com.rentease.entity.Booking;
import com.rentease.entity.RentalAgreement;
import com.rentease.entity.Role;
import com.rentease.entity.User;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.exception.UnauthorizedException;
import com.rentease.repository.RentalAgreementRepository;
import com.rentease.service.RentalAgreementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalAgreementServiceImpl implements RentalAgreementService {

    private final RentalAgreementRepository rentalAgreementRepository;

    public RentalAgreementServiceImpl(RentalAgreementRepository rentalAgreementRepository) {
        this.rentalAgreementRepository = rentalAgreementRepository;
    }

    @Override
    @Transactional
    public RentalAgreement generateAgreement(Booking booking) {
        // Return existing agreement if already generated
        var existing = rentalAgreementRepository.findByBookingId(booking.getId());
        if (existing.isPresent()) {
            return existing.get();
        }

        RentalAgreement agreement = new RentalAgreement();
        String agreementNumber = "AGR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        agreement.setAgreementNumber(agreementNumber);
        agreement.setBooking(booking);
        agreement.setTenant(booking.getTenant());
        agreement.setOwner(booking.getOwner());
        agreement.setProperty(booking.getProperty());
        agreement.setMonthlyRent(booking.getMonthlyRent());
        agreement.setSecurityDeposit(booking.getSecurityDeposit());
        agreement.setStartDate(booking.getStartDate());
        agreement.setEndDate(booking.getEndDate());
        agreement.setAgreementDate(LocalDate.now());
        agreement.setStatus("ACTIVE");

        String terms = buildStandardTerms(booking, agreementNumber);
        agreement.setTermsAndConditions(terms);

        return rentalAgreementRepository.save(agreement);
    }

    private String buildStandardTerms(Booking booking, String agreementNumber) {
        return "RESIDENTIAL PROPERTY LEASE AGREEMENT\n\n" +
                "Agreement Reference: " + agreementNumber + "\n" +
                "Effective Date: " + LocalDate.now() + "\n\n" +
                "1. PARTIES INVOLVED:\n" +
                "This agreement is made between " + booking.getOwner().getName() + " (hereafter 'Landlord/Owner') " +
                "and " + booking.getTenant().getName() + " (hereafter 'Tenant').\n\n" +
                "2. DEMISED PROPERTY:\n" +
                "The Landlord hereby leases the property situated at: " +
                booking.getProperty().getAddress() + ", " + booking.getProperty().getCity() + ", " +
                booking.getProperty().getState() + " - " + booking.getProperty().getPincode() + ".\n\n" +
                "3. TERM OF TENANCY:\n" +
                "The tenancy shall commence on " + booking.getStartDate() + " and expire on " + booking.getEndDate() + ".\n\n" +
                "4. RENTAL PAYMENTS & SECURITY DEPOSIT:\n" +
                "- Monthly Rent: INR " + booking.getMonthlyRent() + " payable in advance.\n" +
                "- Refundable Security Deposit: INR " + booking.getSecurityDeposit() + ".\n\n" +
                "5. USE & COVENANTS:\n" +
                "The Tenant agrees to utilize the premises strictly for residential living, avoid creating nuisance, and keep the interior and fixtures intact.\n\n" +
                "6. NOTICE PERIOD FOR VACATING:\n" +
                "Either party may terminate this lease with thirty (30) days prior written notice.\n\n" +
                "Digitally Verified and Executed via RentEase Secure Platform.";
    }

    @Override
    @Transactional(readOnly = true)
    public RentalAgreementResponse getAgreementByBookingId(Long bookingId, User currentUser) {
        RentalAgreement agreement = rentalAgreementRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental agreement not found for booking id: " + bookingId));

        if (!agreement.getTenant().getId().equals(currentUser.getId()) &&
            !agreement.getOwner().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to view this agreement");
        }

        return new RentalAgreementResponse(agreement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementResponse> getTenantAgreements(User tenant) {
        return rentalAgreementRepository.findByTenantIdOrderByCreatedAtDesc(tenant.getId()).stream()
                .map(RentalAgreementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementResponse> getOwnerAgreements(User owner) {
        return rentalAgreementRepository.findByOwnerIdOrderByCreatedAtDesc(owner.getId()).stream()
                .map(RentalAgreementResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByBookingId(Long bookingId) {
        return rentalAgreementRepository.findByBookingId(bookingId).isPresent();
    }
}
