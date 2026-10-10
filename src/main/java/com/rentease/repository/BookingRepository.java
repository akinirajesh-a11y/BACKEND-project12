package com.rentease.repository;

import com.rentease.entity.Booking;
import com.rentease.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByTenantIdOrderByCreatedAtDesc(Long tenantId);

    List<Booking> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Booking> findByPropertyId(Long propertyId);

    List<Booking> findAllByOrderByCreatedAtDesc();

    long countByBookingStatus(BookingStatus bookingStatus);

    long countByOwnerId(Long ownerId);

    long countByTenantId(Long tenantId);

    long countByOwnerIdAndBookingStatus(Long ownerId, BookingStatus bookingStatus);

    long countByTenantIdAndBookingStatus(Long tenantId, BookingStatus bookingStatus);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.property.id = :propertyId " +
           "AND b.bookingStatus IN ('APPROVED', 'COMPLETED') " +
           "AND (:startDate <= b.endDate AND :endDate >= b.startDate)")
    long countOverlappingBookings(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
