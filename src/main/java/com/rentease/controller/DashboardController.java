package com.rentease.controller;

import com.rentease.dto.ApiResponse;
import com.rentease.dto.OwnerDashboardStats;
import com.rentease.dto.TenantDashboardStats;
import com.rentease.entity.User;
import com.rentease.service.AdminService;
import com.rentease.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final AdminService adminService;
    private final AuthService authService;

    public DashboardController(AdminService adminService, AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OwnerDashboardStats>> getOwnerDashboardStats() {
        User currentUser = authService.getCurrentUser();
        OwnerDashboardStats stats = adminService.getOwnerDashboardStats(currentUser);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/tenant")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ApiResponse<TenantDashboardStats>> getTenantDashboardStats() {
        User currentUser = authService.getCurrentUser();
        TenantDashboardStats stats = adminService.getTenantDashboardStats(currentUser);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
