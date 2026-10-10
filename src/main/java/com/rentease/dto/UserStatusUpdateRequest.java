package com.rentease.dto;

import jakarta.validation.constraints.NotNull;

public class UserStatusUpdateRequest {

    @NotNull(message = "Blocked status is required")
    private Boolean blocked;

    public UserStatusUpdateRequest() {
    }

    public UserStatusUpdateRequest(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}
