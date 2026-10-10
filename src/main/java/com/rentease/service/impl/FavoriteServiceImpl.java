package com.rentease.service.impl;

import com.rentease.dto.PropertyResponse;
import com.rentease.entity.Favorite;
import com.rentease.entity.Property;
import com.rentease.entity.User;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.repository.FavoriteRepository;
import com.rentease.repository.PropertyRepository;
import com.rentease.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, PropertyRepository propertyRepository) {
        this.favoriteRepository = favoriteRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    @Transactional
    public void addFavorite(Long propertyId, User user) {
        if (favoriteRepository.existsByUserIdAndPropertyId(user.getId(), propertyId)) {
            return;
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        Favorite favorite = new Favorite(user, property);
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long propertyId, User user) {
        favoriteRepository.deleteByUserIdAndPropertyId(user.getId(), propertyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getUserFavorites(User user) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(fav -> new PropertyResponse(fav.getProperty()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(Long propertyId, User user) {
        return favoriteRepository.existsByUserIdAndPropertyId(user.getId(), propertyId);
    }
}
