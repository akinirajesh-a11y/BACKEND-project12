package com.rentease.service.impl;

import com.rentease.dto.AdminDashboardStats;
import com.rentease.dto.OwnerDashboardStats;
import com.rentease.dto.TenantDashboardStats;
import com.rentease.dto.UserDTO;
import com.rentease.entity.*;
import com.rentease.exception.BadRequestException;
import com.rentease.exception.ResourceNotFoundException;
import com.rentease.repository.*;
import com.rentease.service.AdminService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final FavoriteRepository favoriteRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            PropertyRepository propertyRepository,
                            BookingRepository bookingRepository,
                            PaymentRepository paymentRepository,
                            FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardStats getDashboardStats() {
        AdminDashboardStats stats = new AdminDashboardStats();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalOwners(userRepository.countByRole(Role.ROLE_OWNER));
        stats.setTotalTenants(userRepository.countByRole(Role.ROLE_TENANT));
        stats.setTotalProperties(propertyRepository.count());
        stats.setPendingProperties(propertyRepository.countByStatus(PropertyStatus.PENDING));
        stats.setApprovedProperties(propertyRepository.countByStatus(PropertyStatus.APPROVED));
        stats.setTotalBookings(bookingRepository.count());
        stats.setActiveRentals(bookingRepository.countByBookingStatus(BookingStatus.COMPLETED));
        stats.setSuccessfulPayments(paymentRepository.countByPaymentStatus(PaymentStatus.SUCCESS));
        stats.setTotalRevenue(paymentRepository.sumTotalSuccessfulPayments());
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUserStatus(Long userId, boolean blocked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException("Administrator accounts cannot be blocked");
        }

        user.setBlocked(blocked);
        User updated = userRepository.save(user);
        return new UserDTO(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException("Administrator accounts cannot be deleted");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerDashboardStats getOwnerDashboardStats(User owner) {
        OwnerDashboardStats stats = new OwnerDashboardStats();
        stats.setTotalProperties(propertyRepository.countByOwnerId(owner.getId()));
        stats.setPendingRequests(bookingRepository.countByOwnerIdAndBookingStatus(owner.getId(), BookingStatus.PENDING));
        stats.setApprovedBookings(bookingRepository.countByOwnerIdAndBookingStatus(owner.getId(), BookingStatus.APPROVED));
        stats.setActiveRentals(bookingRepository.countByOwnerIdAndBookingStatus(owner.getId(), BookingStatus.COMPLETED));
        stats.setTotalEarnings(paymentRepository.sumEarningsByOwnerId(owner.getId()));
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public TenantDashboardStats getTenantDashboardStats(User tenant) {
        TenantDashboardStats stats = new TenantDashboardStats();
        stats.setTotalBookings(bookingRepository.countByTenantId(tenant.getId()));
        stats.setActiveRentals(bookingRepository.countByTenantIdAndBookingStatus(tenant.getId(), BookingStatus.COMPLETED));
        stats.setPendingRequests(bookingRepository.countByTenantIdAndBookingStatus(tenant.getId(), BookingStatus.PENDING));
        stats.setTotalFavorites(favoriteRepository.findByUserIdOrderByCreatedAtDesc(tenant.getId()).size());

        List<Payment> tenantPayments = paymentRepository.findByTenantIdOrderByPaymentDateDesc(tenant.getId());
        BigDecimal totalPaid = tenantPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalAmountPaid(totalPaid);

        return stats;
    }
}
