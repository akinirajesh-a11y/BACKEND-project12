package com.rentease.service.impl;

import com.rentease.dto.PropertyRequest;
import com.rentease.dto.PropertyResponse;
import com.rentease.entity.*;
import com.rentease.exception.BadRequestException;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.exception.UnauthorizedException;
import com.rentease.repository.PropertyImageRepository;
import com.rentease.repository.PropertyRepository;
import com.rentease.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyImageRepository propertyImageRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
    }

    @Override
    @Transactional
    public PropertyResponse createProperty(PropertyRequest request, User owner) {
        if (owner.getRole() != Role.ROLE_OWNER && owner.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only property owners and administrators can list properties");
        }

        Property property = new Property();
        property.setOwner(owner);
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPropertyType(request.getPropertyType());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setPincode(request.getPincode());
        property.setMonthlyRent(request.getMonthlyRent());
        property.setSecurityDeposit(request.getSecurityDeposit());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setFurnishedStatus(request.getFurnishedStatus());
        property.setAmenities(request.getAmenities());
        // Auto-approve newly created listings so owners can immediately view and rent them
        property.setStatus(PropertyStatus.APPROVED);
        property.setAvailable(true);

        Property savedProperty = propertyRepository.save(property);

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            boolean isFirst = true;
            for (String url : request.getImageUrls()) {
                if (url != null && !url.isBlank()) {
                    PropertyImage image = new PropertyImage(savedProperty, url.trim(), isFirst);
                    propertyImageRepository.save(image);
                    savedProperty.addImage(image);
                    isFirst = false;
                }
            }
        }

        return new PropertyResponse(savedProperty);
    }

    @Override
    @Transactional
    public PropertyResponse updateProperty(Long id, PropertyRequest request, User currentUser) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        // Check ownership or admin
        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to update this property");
        }

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPropertyType(request.getPropertyType());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setPincode(request.getPincode());
        property.setMonthlyRent(request.getMonthlyRent());
        property.setSecurityDeposit(request.getSecurityDeposit());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setFurnishedStatus(request.getFurnishedStatus());
        property.setAmenities(request.getAmenities());

        if (request.getImageUrls() != null) {
            propertyImageRepository.deleteByPropertyId(property.getId());
            property.getImages().clear();

            boolean isFirst = true;
            for (String url : request.getImageUrls()) {
                if (url != null && !url.isBlank()) {
                    PropertyImage image = new PropertyImage(property, url.trim(), isFirst);
                    propertyImageRepository.save(image);
                    property.addImage(image);
                    isFirst = false;
                }
            }
        }

        Property updatedProperty = propertyRepository.save(property);
        return new PropertyResponse(updatedProperty);
    }

    @Override
    @Transactional
    public void deleteProperty(Long id, User currentUser) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to delete this property");
        }

        propertyRepository.delete(property);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return new PropertyResponse(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyResponse> searchProperties(String city,
                                                  PropertyType propertyType,
                                                  BigDecimal minRent,
                                                  BigDecimal maxRent,
                                                  Integer bedrooms,
                                                  FurnishedStatus furnishedStatus,
                                                  Pageable pageable) {
        String cityParam = (city != null && !city.isBlank()) ? city.trim() : null;
        Page<Property> propertyPage = propertyRepository.searchProperties(
                cityParam, propertyType, minRent, maxRent, bedrooms, furnishedStatus, pageable);
        return propertyPage.map(PropertyResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getFeaturedProperties() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "createdAt"));
        return propertyRepository.searchProperties(null, null, null, null, null, null, pageable)
                .getContent().stream()
                .map(PropertyResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getOwnerProperties(User owner) {
        return propertyRepository.findByOwnerIdOrderByCreatedAtDesc(owner.getId()).stream()
                .map(PropertyResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllPropertiesForAdmin() {
        return propertyRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(PropertyResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PropertyResponse approveProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        property.setStatus(PropertyStatus.APPROVED);
        return new PropertyResponse(propertyRepository.save(property));
    }

    @Override
    @Transactional
    public PropertyResponse rejectProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        property.setStatus(PropertyStatus.REJECTED);
        return new PropertyResponse(propertyRepository.save(property));
    }

    @Override
    @Transactional
    public PropertyResponse toggleAvailability(Long id, User owner) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        if (!property.getOwner().getId().equals(owner.getId()) && owner.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to update this property");
        }

        property.setAvailable(!property.isAvailable());
        return new PropertyResponse(propertyRepository.save(property));
    }
}
