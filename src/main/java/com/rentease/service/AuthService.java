package com.rentease.service;

import com.rentease.dto.AuthResponse;
import com.rentease.dto.LoginRequest;
import com.rentease.dto.RegisterRequest;
import com.rentease.dto.UserDTO;
import com.rentease.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User getCurrentUser();
    UserDTO getCurrentUserDTO();
    UserDTO updateProfile(UserDTO userDTO);
    void changePassword(String currentPassword, String newPassword);
}
