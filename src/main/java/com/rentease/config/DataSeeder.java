package com.rentease.config;

import com.rentease.entity.*;
import com.rentease.repository.*;
import com.rentease.service.RentalAgreementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final RentalAgreementService rentalAgreementService;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      PropertyRepository propertyRepository,
                      PropertyImageRepository propertyImageRepository,
                      BookingRepository bookingRepository,
                      PaymentRepository paymentRepository,
                      RentalAgreementService rentalAgreementService,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.rentalAgreementService = rentalAgreementService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Already seeded
        }

        // 1. Create Users
        User admin = new User("System Admin", "admin@rentease.com", passwordEncoder.encode("admin123"), "+91 99999 00001", Role.ROLE_ADMIN);
        User owner1 = new User("Vikram Malhotra", "owner@rentease.com", passwordEncoder.encode("owner123"), "+91 98765 43210", Role.ROLE_OWNER);
        User owner2 = new User("Priya Sharma", "priya.sharma@rentease.com", passwordEncoder.encode("owner123"), "+91 98765 11223", Role.ROLE_OWNER);
        User tenant1 = new User("Rahul Verma", "tenant@rentease.com", passwordEncoder.encode("tenant123"), "+91 91234 56789", Role.ROLE_TENANT);
        User tenant2 = new User("Sneha Patel", "sneha.patel@rentease.com", passwordEncoder.encode("tenant123"), "+91 99887 76655", Role.ROLE_TENANT);

        admin = userRepository.save(admin);
        owner1 = userRepository.save(owner1);
        owner2 = userRepository.save(owner2);
        tenant1 = userRepository.save(tenant1);
        tenant2 = userRepository.save(tenant2);

        // 2. Create Properties
        // Property 1: Mumbai Penthouse
        Property p1 = new Property();
        p1.setOwner(owner1);
        p1.setTitle("Skyline Luxury 3BHK Penthouse with Sea View");
        p1.setDescription("Breathtaking ultra-luxury 3BHK penthouse overlooking the Arabian Sea in Bandra West. Features Italian marble flooring, floor-to-ceiling glass windows, designer Italian modular kitchen, private elevator access, and rooftop terrace.");
        p1.setPropertyType(PropertyType.APARTMENT);
        p1.setAddress("Carter Road, Bandra West");
        p1.setCity("Mumbai");
        p1.setState("Maharashtra");
        p1.setPincode("400050");
        p1.setMonthlyRent(new BigDecimal("85000.00"));
        p1.setSecurityDeposit(new BigDecimal("170000.00"));
        p1.setBedrooms(3);
        p1.setBathrooms(3);
        p1.setAreaSqFt(1850.0);
        p1.setFurnishedStatus(FurnishedStatus.FURNISHED);
        p1.setAmenities("Swimming Pool,Gymnasium,24/7 Security,Covered Car Parking,Sea View Balcony,High-Speed WiFi,Centralized AC,Clubhouse");
        p1.setStatus(PropertyStatus.APPROVED);
        p1.setAvailable(true);
        p1 = propertyRepository.save(p1);

        addImages(p1,
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1600566753376-12c8ab7fb75b?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1600573472591-ee6b68d14c68?auto=format&fit=crop&w=1200&q=80"
        );

        // Property 2: Bangalore Indiranagar Condo
        Property p2 = new Property();
        p2.setOwner(owner1);
        p2.setTitle("Modern High-Rise 2BHK Condo in Indiranagar");
        p2.setDescription("Sunlit contemporary 2BHK apartment in prime Indiranagar. Walking distance to 100ft road cafes, metro station, and tech parks. Equipped with modern wooden furnishings, dedicated work desk, and balcony garden.");
        p2.setPropertyType(PropertyType.APARTMENT);
        p2.setAddress("12th Main Road, HAL 2nd Stage, Indiranagar");
        p2.setCity("Bangalore");
        p2.setState("Karnataka");
        p2.setPincode("560038");
        p2.setMonthlyRent(new BigDecimal("42000.00"));
        p2.setSecurityDeposit(new BigDecimal("84000.00"));
        p2.setBedrooms(2);
        p2.setBathrooms(2);
        p2.setAreaSqFt(1200.0);
        p2.setFurnishedStatus(FurnishedStatus.FURNISHED);
        p2.setAmenities("Clubhouse,Power Backup,Gymnasium,EV Charging Point,Children Play Area,Gated Security,High-Speed WiFi");
        p2.setStatus(PropertyStatus.APPROVED);
        p2.setAvailable(true);
        p2 = propertyRepository.save(p2);

        addImages(p2,
                "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1613490493576-7fde63acd811?auto=format&fit=crop&w=1200&q=80"
        );

        // Property 3: Bangalore Villa
        Property p3 = new Property();
        p3.setOwner(owner2);
        p3.setTitle("Serene 4BHK Garden Villa with Private Pool");
        p3.setDescription("Expansive luxury independent villa situated in Whitefield. Includes private heated plunge pool, lush landscaped garden, servant quarters, home theatre, and solar-powered smart home automation.");
        p3.setPropertyType(PropertyType.VILLA);
        p3.setAddress("Prestige Palm Springs, Whitefield Main Rd");
        p3.setCity("Bangalore");
        p3.setState("Karnataka");
        p3.setPincode("560066");
        p3.setMonthlyRent(new BigDecimal("120000.00"));
        p3.setSecurityDeposit(new BigDecimal("240000.00"));
        p3.setBedrooms(4);
        p3.setBathrooms(4);
        p3.setAreaSqFt(3200.0);
        p3.setFurnishedStatus(FurnishedStatus.FURNISHED);
        p3.setAmenities("Private Swimming Pool,Private Garden,Solar Power,Smart Automation,Servant Quarters,Gated Community,Tennis Court");
        p3.setStatus(PropertyStatus.APPROVED);
        p3.setAvailable(true);
        p3 = propertyRepository.save(p3);

        addImages(p3,
                "https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=1200&q=80"
        );

        // Property 4: Delhi Urban Studio
        Property p4 = new Property();
        p4.setOwner(owner2);
        p4.setTitle("Chic Minimalist Studio near Metro Station");
        p4.setDescription("Fully equipped studio apartment ideal for solo professionals and digital nomads. Centrally located with 2-minute walk to Rajiv Chowk metro. Includes queen bed, workstation, smart TV, and induction kitchenette.");
        p4.setPropertyType(PropertyType.STUDIO);
        p4.setAddress("Barakhamba Road, Connaught Place");
        p4.setCity("New Delhi");
        p4.setState("Delhi");
        p4.setPincode("110001");
        p4.setMonthlyRent(new BigDecimal("26000.00"));
        p4.setSecurityDeposit(new BigDecimal("52000.00"));
        p4.setBedrooms(1);
        p4.setBathrooms(1);
        p4.setAreaSqFt(550.0);
        p4.setFurnishedStatus(FurnishedStatus.FURNISHED);
        p4.setAmenities("Air Conditioning,High-Speed WiFi,Metro Connectivity,Housekeeping,Elevator,CCTV Security");
        p4.setStatus(PropertyStatus.APPROVED);
        p4.setAvailable(true);
        p4 = propertyRepository.save(p4);

        addImages(p4,
                "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=1200&q=80"
        );

        // Property 5: Pune Independent House
        Property p5 = new Property();
        p5.setOwner(owner1);
        p5.setTitle("Elegant 3BHK Independent Row House with Terrace");
        p5.setDescription("Peaceful residential row house in prime Koregaon Park. Large private terrace, shaded porch, modular wardrobes, and close proximity to Osho Garden and international schools.");
        p5.setPropertyType(PropertyType.HOUSE);
        p5.setAddress("Lane 5, Koregaon Park");
        p5.setCity("Pune");
        p5.setState("Maharashtra");
        p5.setPincode("411001");
        p5.setMonthlyRent(new BigDecimal("48000.00"));
        p5.setSecurityDeposit(new BigDecimal("96000.00"));
        p5.setBedrooms(3);
        p5.setBathrooms(3);
        p5.setAreaSqFt(1650.0);
        p5.setFurnishedStatus(FurnishedStatus.SEMI_FURNISHED);
        p5.setAmenities("Private Terrace,Pet Friendly,Car Parking,Water Purifier,Gated Security,Visitor Parking");
        p5.setStatus(PropertyStatus.APPROVED);
        p5.setAvailable(true);
        p5 = propertyRepository.save(p5);

        addImages(p5,
                "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=1200&q=80"
        );

        // Property 6: Hyderabad Executive PG
        Property p6 = new Property();
        p6.setOwner(owner2);
        p6.setTitle("Executive Co-Living PG Room in Gachibowli");
        p6.setDescription("All-inclusive modern co-living space located in the heart of Hyderabad Financial District. Includes 3 nutritious meals daily, weekly laundry, high-speed fiber internet, and co-working lounge.");
        p6.setPropertyType(PropertyType.PG);
        p6.setAddress("Near DLF Cyber City, Gachibowli");
        p6.setCity("Hyderabad");
        p6.setState("Telangana");
        p6.setPincode("500032");
        p6.setMonthlyRent(new BigDecimal("16500.00"));
        p6.setSecurityDeposit(new BigDecimal("25000.00"));
        p6.setBedrooms(1);
        p6.setBathrooms(1);
        p6.setAreaSqFt(350.0);
        p6.setFurnishedStatus(FurnishedStatus.FURNISHED);
        p6.setAmenities("3 Meals Included,Daily Housekeeping,High-Speed WiFi,Gaming Lounge,Air Conditioning,24/7 Power Backup");
        p6.setStatus(PropertyStatus.APPROVED);
        p6.setAvailable(true);
        p6 = propertyRepository.save(p6);

        addImages(p6,
                "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=1200&q=80",
                "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?auto=format&fit=crop&w=1200&q=80"
        );

        // 3. Create Sample Completed Booking with Payment and Rental Agreement
        Booking completedBooking = new Booking();
        completedBooking.setBookingNumber("BK-DEMO1001");
        completedBooking.setProperty(p2);
        completedBooking.setTenant(tenant1);
        completedBooking.setOwner(p2.getOwner());
        completedBooking.setStartDate(LocalDate.now().plusDays(5));
        completedBooking.setEndDate(LocalDate.now().plusDays(370));
        completedBooking.setMonthlyRent(p2.getMonthlyRent());
        completedBooking.setSecurityDeposit(p2.getSecurityDeposit());
        completedBooking.setTotalAmount(p2.getMonthlyRent().add(p2.getSecurityDeposit()));
        completedBooking.setBookingStatus(BookingStatus.COMPLETED);
        completedBooking.setPaymentStatus(PaymentStatus.SUCCESS);
        completedBooking.setSpecialRequests("Early key handover preferred if possible.");
        completedBooking = bookingRepository.save(completedBooking);

        // Payment for completed booking
        Payment payment = new Payment();
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        payment.setBooking(completedBooking);
        payment.setTenant(tenant1);
        payment.setAmount(completedBooking.getTotalAmount());
        payment.setPaymentMethod(PaymentMethod.CARD);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now().minusDays(1));
        payment.setGatewayReference("SIM-HDFC-991823");
        paymentRepository.save(payment);

        // Generate official rental agreement
        rentalAgreementService.generateAgreement(completedBooking);

        // 4. Create Sample Pending Booking Request
        Booking pendingBooking = new Booking();
        pendingBooking.setBookingNumber("BK-DEMO1002");
        pendingBooking.setProperty(p1);
        pendingBooking.setTenant(tenant2);
        pendingBooking.setOwner(p1.getOwner());
        pendingBooking.setStartDate(LocalDate.now().plusDays(10));
        pendingBooking.setEndDate(LocalDate.now().plusDays(375));
        pendingBooking.setMonthlyRent(p1.getMonthlyRent());
        pendingBooking.setSecurityDeposit(p1.getSecurityDeposit());
        pendingBooking.setTotalAmount(p1.getMonthlyRent().add(p1.getSecurityDeposit()));
        pendingBooking.setBookingStatus(BookingStatus.PENDING);
        pendingBooking.setPaymentStatus(PaymentStatus.PENDING);
        pendingBooking.setSpecialRequests("Family with 1 kid. Please let us know the society moving-in guidelines.");
        bookingRepository.save(pendingBooking);
    }

    private void addImages(Property property, String... urls) {
        boolean first = true;
        for (String url : urls) {
            PropertyImage img = new PropertyImage(property, url, first);
            propertyImageRepository.save(img);
            property.addImage(img);
            first = false;
        }
    }
}
