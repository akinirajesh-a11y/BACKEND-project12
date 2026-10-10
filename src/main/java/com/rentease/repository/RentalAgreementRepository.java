package com.rentease.repository;

import com.rentease.entity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {

    Optional<RentalAgreement> findByAgreementNumber(String agreementNumber);

    Optional<RentalAgreement> findByBookingId(Long bookingId);

    List<RentalAgreement> findByTenantIdOrderByCreatedAtDesc(Long tenantId);

    List<RentalAgreement> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    long countByStatus(String status);
}
