package com.rentease.controller;

import com.rentease.dto.ApiResponse;
import com.rentease.dto.RentalAgreementResponse;
import com.rentease.entity.Role;
import com.rentease.entity.User;
import com.rentease.service.AuthService;
import com.rentease.service.RentalAgreementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agreements")
public class RentalAgreementController {

    private final RentalAgreementService rentalAgreementService;
    private final AuthService authService;

    public RentalAgreementController(RentalAgreementService rentalAgreementService, AuthService authService) {
        this.rentalAgreementService = rentalAgreementService;
        this.authService = authService;
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<RentalAgreementResponse>> getAgreementByBookingId(@PathVariable Long bookingId) {
        User currentUser = authService.getCurrentUser();
        RentalAgreementResponse agreement = rentalAgreementService.getAgreementByBookingId(bookingId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(agreement));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<RentalAgreementResponse>>> getMyAgreements() {
        User currentUser = authService.getCurrentUser();
        List<RentalAgreementResponse> agreements;
        if (currentUser.getRole() == Role.ROLE_OWNER) {
            agreements = rentalAgreementService.getOwnerAgreements(currentUser);
        } else {
            agreements = rentalAgreementService.getTenantAgreements(currentUser);
        }
        return ResponseEntity.ok(ApiResponse.success(agreements));
    }
}
