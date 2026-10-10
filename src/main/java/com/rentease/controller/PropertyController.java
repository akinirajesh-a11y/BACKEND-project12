package com.rentease.controller;

import com.rentease.dto.ApiResponse;
import com.rentease.dto.PropertyRequest;
import com.rentease.dto.PropertyResponse;
import com.rentease.entity.FurnishedStatus;
import com.rentease.entity.PropertyType;
import com.rentease.entity.User;
import com.rentease.service.AuthService;
import com.rentease.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final AuthService authService;

    public PropertyController(PropertyService propertyService, AuthService authService) {
        this.propertyService = propertyService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PropertyResponse>>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) FurnishedStatus furnishedStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PropertyResponse> properties = propertyService.searchProperties(
                city, propertyType, minRent, maxRent, bedrooms, furnishedStatus, pageable);

        return ResponseEntity.ok(ApiResponse.success(properties));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getFeaturedProperties() {
        List<PropertyResponse> featured = propertyService.getFeaturedProperties();
        return ResponseEntity.ok(ApiResponse.success(featured));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(@PathVariable Long id) {
        PropertyResponse property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(ApiResponse.success(property));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getMyProperties() {
        User currentUser = authService.getCurrentUser();
        List<PropertyResponse> properties = propertyService.getOwnerProperties(currentUser);
        return ResponseEntity.ok(ApiResponse.success(properties));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(@Valid @RequestBody PropertyRequest request) {
        User currentUser = authService.getCurrentUser();
        PropertyResponse created = propertyService.createProperty(request, currentUser);
        return new ResponseEntity<>(ApiResponse.success("Property created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PropertyResponse>> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest request
    ) {
        User currentUser = authService.getCurrentUser();
        PropertyResponse updated = propertyService.updateProperty(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Property updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProperty(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        propertyService.deleteProperty(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Property deleted successfully", null));
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PropertyResponse>> toggleAvailability(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        PropertyResponse response = propertyService.toggleAvailability(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Property availability updated", response));
    }
}
