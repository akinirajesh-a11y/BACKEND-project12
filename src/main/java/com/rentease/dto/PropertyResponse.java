package com.rentease.dto;

import com.rentease.entity.FurnishedStatus;
import com.rentease.entity.Property;
import com.rentease.entity.PropertyStatus;
import com.rentease.entity.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private PropertyType propertyType;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private BigDecimal monthlyRent;
    private BigDecimal securityDeposit;
    private int bedrooms;
    private int bathrooms;
    private double areaSqFt;
    private FurnishedStatus furnishedStatus;
    private String amenities;
    private PropertyStatus status;
    private boolean available;
    private UserDTO owner;
    private List<PropertyImageDTO> images = new ArrayList<>();
    private String primaryImageUrl;
    private LocalDateTime createdAt;

    public PropertyResponse() {
    }

    public PropertyResponse(Property property) {
        if (property != null) {
            this.id = property.getId();
            this.title = property.getTitle();
            this.description = property.getDescription();
            this.propertyType = property.getPropertyType();
            this.address = property.getAddress();
            this.city = property.getCity();
            this.state = property.getState();
            this.pincode = property.getPincode();
            this.monthlyRent = property.getMonthlyRent();
            this.securityDeposit = property.getSecurityDeposit();
            this.bedrooms = property.getBedrooms();
            this.bathrooms = property.getBathrooms();
            this.areaSqFt = property.getAreaSqFt();
            this.furnishedStatus = property.getFurnishedStatus();
            this.amenities = property.getAmenities();
            this.status = property.getStatus();
            this.available = property.isAvailable();
            if (property.getOwner() != null) {
                this.owner = new UserDTO(property.getOwner());
            }
            if (property.getImages() != null && !property.getImages().isEmpty()) {
                this.images = property.getImages().stream()
                        .map(PropertyImageDTO::new)
                        .collect(Collectors.toList());
                this.primaryImageUrl = property.getImages().stream()
                        .filter(img -> img.isPrimary())
                        .map(img -> img.getImageUrl())
                        .findFirst()
                        .orElse(property.getImages().get(0).getImageUrl());
            }
            this.createdAt = property.getCreatedAt();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public double getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(double areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public FurnishedStatus getFurnishedStatus() {
        return furnishedStatus;
    }

    public void setFurnishedStatus(FurnishedStatus furnishedStatus) {
        this.furnishedStatus = furnishedStatus;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public List<PropertyImageDTO> getImages() {
        return images;
    }

    public void setImages(List<PropertyImageDTO> images) {
        this.images = images;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
