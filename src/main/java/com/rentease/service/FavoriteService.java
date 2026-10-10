package com.rentease.service;

import com.rentease.dto.PropertyResponse;
import com.rentease.entity.User;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long propertyId, User user);
    void removeFavorite(Long propertyId, User user);
    List<PropertyResponse> getUserFavorites(User user);
    boolean isFavorite(Long propertyId, User user);
}
