package com.rentease.service;

import com.rentease.dto.PropertyRequest;
import com.rentease.dto.PropertyResponse;
import com.rentease.entity.FurnishedStatus;
import com.rentease.entity.PropertyType;
import com.rentease.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyService {
    PropertyResponse createProperty(PropertyRequest request, User owner);
    PropertyResponse updateProperty(Long id, PropertyRequest request, User currentUser);
    void deleteProperty(Long id, User currentUser);
    PropertyResponse getPropertyById(Long id);
    Page<PropertyResponse> searchProperties(String city,
                                           PropertyType propertyType,
                                           BigDecimal minRent,
                                           BigDecimal maxRent,
                                           Integer bedrooms,
                                           FurnishedStatus furnishedStatus,
                                           Pageable pageable);
    List<PropertyResponse> getFeaturedProperties();
    List<PropertyResponse> getOwnerProperties(User owner);
    List<PropertyResponse> getAllPropertiesForAdmin();
    PropertyResponse approveProperty(Long id);
    PropertyResponse rejectProperty(Long id);
    PropertyResponse toggleAvailability(Long id, User owner);
}
