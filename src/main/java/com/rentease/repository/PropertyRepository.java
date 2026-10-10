package com.rentease.repository;

import com.rentease.entity.FurnishedStatus;
import com.rentease.entity.Property;
import com.rentease.entity.PropertyStatus;
import com.rentease.entity.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerId(Long ownerId);

    List<Property> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<Property> findByStatus(PropertyStatus status);

    List<Property> findByStatusOrderByCreatedAtDesc(PropertyStatus status);

    long countByStatus(PropertyStatus status);

    long countByOwnerId(Long ownerId);

    @Query("SELECT p FROM Property p WHERE p.status = 'APPROVED' AND p.available = true " +
           "AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')) OR LOWER(p.address) LIKE LOWER(CONCAT('%', :city, '%')) OR LOWER(p.state) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
           "AND (:minRent IS NULL OR p.monthlyRent >= :minRent) " +
           "AND (:maxRent IS NULL OR p.monthlyRent <= :maxRent) " +
           "AND (:bedrooms IS NULL OR p.bedrooms = :bedrooms) " +
           "AND (:furnishedStatus IS NULL OR p.furnishedStatus = :furnishedStatus)")
    Page<Property> searchProperties(
            @Param("city") String city,
            @Param("propertyType") PropertyType propertyType,
            @Param("minRent") BigDecimal minRent,
            @Param("maxRent") BigDecimal maxRent,
            @Param("bedrooms") Integer bedrooms,
            @Param("furnishedStatus") FurnishedStatus furnishedStatus,
            Pageable pageable
    );
}
