package com.rentease.dto;

import java.math.BigDecimal;

public class AdminDashboardStats {

    private long totalUsers;
    private long totalOwners;
    private long totalTenants;
    private long totalProperties;
    private long pendingProperties;
    private long approvedProperties;
    private long totalBookings;
    private long activeRentals;
    private long successfulPayments;
    private BigDecimal totalRevenue;

    public AdminDashboardStats() {
        this.totalRevenue = BigDecimal.ZERO;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(long totalOwners) {
        this.totalOwners = totalOwners;
    }

    public long getTotalTenants() {
        return totalTenants;
    }

    public void setTotalTenants(long totalTenants) {
        this.totalTenants = totalTenants;
    }

    public long getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(long totalProperties) {
        this.totalProperties = totalProperties;
    }

    public long getPendingProperties() {
        return pendingProperties;
    }

    public void setPendingProperties(long pendingProperties) {
        this.pendingProperties = pendingProperties;
    }

    public long getApprovedProperties() {
        return approvedProperties;
    }

    public void setApprovedProperties(long approvedProperties) {
        this.approvedProperties = approvedProperties;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getActiveRentals() {
        return activeRentals;
    }

    public void setActiveRentals(long activeRentals) {
        this.activeRentals = activeRentals;
    }

    public long getSuccessfulPayments() {
        return successfulPayments;
    }

    public void setSuccessfulPayments(long successfulPayments) {
        this.successfulPayments = successfulPayments;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
