package com.rentease.service;

import com.rentease.dto.PropertyRequest;
import com.rentease.dto.PropertyResponse;
import com.rentease.entity.*;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.repository.PropertyImageRepository;
import com.rentease.repository.PropertyRepository;
import com.rentease.service.impl.PropertyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private PropertyImageRepository propertyImageRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private User owner;
    private Property property;

    @BeforeEach
    void setUp() {
        owner = new User("Owner Name", "owner@example.com", "pass", "1234567890", Role.ROLE_OWNER);
        owner.setId(10L);

        property = new Property();
        property.setId(100L);
        property.setTitle("Luxury 2BHK");
        property.setDescription("Spacious and central");
        property.setPropertyType(PropertyType.APARTMENT);
        property.setAddress("MG Road");
        property.setCity("Bangalore");
        property.setState("Karnataka");
        property.setPincode("560001");
        property.setMonthlyRent(new BigDecimal("35000.00"));
        property.setSecurityDeposit(new BigDecimal("70000.00"));
        property.setBedrooms(2);
        property.setBathrooms(2);
        property.setAreaSqFt(1100.0);
        property.setFurnishedStatus(FurnishedStatus.FURNISHED);
        property.setStatus(PropertyStatus.APPROVED);
        property.setAvailable(true);
        property.setOwner(owner);
    }

    @Test
    void testCreateProperty_Success() {
        PropertyRequest request = new PropertyRequest();
        request.setTitle("Luxury 2BHK");
        request.setDescription("Spacious and central");
        request.setPropertyType(PropertyType.APARTMENT);
        request.setAddress("MG Road");
        request.setCity("Bangalore");
        request.setState("Karnataka");
        request.setPincode("560001");
        request.setMonthlyRent(new BigDecimal("35000.00"));
        request.setSecurityDeposit(new BigDecimal("70000.00"));
        request.setBedrooms(2);
        request.setBathrooms(2);
        request.setAreaSqFt(1100.0);
        request.setFurnishedStatus(FurnishedStatus.FURNISHED);
        request.setImageUrls(Collections.singletonList("https://images.unsplash.com/sample.jpg"));

        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        PropertyResponse response = propertyService.createProperty(request, owner);

        assertNotNull(response);
        assertEquals("Luxury 2BHK", response.getTitle());
        assertEquals("Bangalore", response.getCity());
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void testGetPropertyById_Success() {
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(property));

        PropertyResponse response = propertyService.getPropertyById(100L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Luxury 2BHK", response.getTitle());
    }

    @Test
    void testGetPropertyById_NotFound_ThrowsException() {
        when(propertyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> propertyService.getPropertyById(999L));
    }
}
