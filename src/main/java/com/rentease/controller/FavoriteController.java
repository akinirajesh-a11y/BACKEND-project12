package com.rentease.controller;

import com.rentease.dto.ApiResponse;
import com.rentease.dto.PropertyResponse;
import com.rentease.entity.User;
import com.rentease.service.AuthService;
import com.rentease.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@PreAuthorize("hasRole('TENANT')")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final AuthService authService;

    public FavoriteController(FavoriteService favoriteService, AuthService authService) {
        this.favoriteService = favoriteService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getUserFavorites() {
        User currentUser = authService.getCurrentUser();
        List<PropertyResponse> favorites = favoriteService.getUserFavorites(currentUser);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<ApiResponse<Void>> addFavorite(@PathVariable Long propertyId) {
        User currentUser = authService.getCurrentUser();
        favoriteService.addFavorite(propertyId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Added to favorites", null));
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(@PathVariable Long propertyId) {
        User currentUser = authService.getCurrentUser();
        favoriteService.removeFavorite(propertyId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Removed from favorites", null));
    }

    @GetMapping("/{propertyId}/check")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> isFavorite(@PathVariable Long propertyId) {
        User currentUser = authService.getCurrentUser();
        boolean isFav = favoriteService.isFavorite(propertyId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(Map.of("isFavorite", isFav)));
    }
}
