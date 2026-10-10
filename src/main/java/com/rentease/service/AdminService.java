package com.rentease.service;

import com.rentease.dto.AdminDashboardStats;
import com.rentease.dto.OwnerDashboardStats;
import com.rentease.dto.TenantDashboardStats;
import com.rentease.dto.UserDTO;
import com.rentease.entity.User;

import java.util.List;

public interface AdminService {
    AdminDashboardStats getDashboardStats();
    List<UserDTO> getAllUsers();
    UserDTO updateUserStatus(Long userId, boolean blocked);
    void deleteUser(Long userId);
    OwnerDashboardStats getOwnerDashboardStats(User owner);
    TenantDashboardStats getTenantDashboardStats(User tenant);
}
