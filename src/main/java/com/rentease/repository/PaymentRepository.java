package com.rentease.repository;

import com.rentease.entity.Payment;
import com.rentease.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByBookingId(Long bookingId);

    List<Payment> findByTenantIdOrderByPaymentDateDesc(Long tenantId);

    List<Payment> findAllByOrderByPaymentDateDesc();

    long countByPaymentStatus(PaymentStatus paymentStatus);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'SUCCESS'")
    BigDecimal sumTotalSuccessfulPayments();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = 'SUCCESS' AND p.booking.owner.id = :ownerId")
    BigDecimal sumEarningsByOwnerId(@Param("ownerId") Long ownerId);
}
