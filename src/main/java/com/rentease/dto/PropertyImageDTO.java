package com.rentease.dto;

import com.rentease.entity.PropertyImage;

public class PropertyImageDTO {

    private Long id;
    private String imageUrl;
    private boolean primary;

    public PropertyImageDTO() {
    }

    public PropertyImageDTO(Long id, String imageUrl, boolean primary) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primary = primary;
    }

    public PropertyImageDTO(PropertyImage image) {
        if (image != null) {
            this.id = image.getId();
            this.imageUrl = image.getImageUrl();
            this.primary = image.isPrimary();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
