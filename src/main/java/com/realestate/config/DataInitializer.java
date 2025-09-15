package com.realestate.config;

import com.realestate.model.entity.Property;
import com.realestate.model.entity.User;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeUsers();
            initializeProperties();
            log.info("Sample data initialization completed!");
        } else {
            log.info("Data already exists, skipping initialization");
        }
    }

    private void initializeUsers() {
        List<User> users = Arrays.asList(
            User.builder()
                .username("admin")
                .email("admin@realestate.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("Admin")
                .lastName("User")
                .phoneNumber("919876543210")
                .role(User.UserRole.ADMIN)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .loginAttempts(0)
                .build(),

            User.builder()
                .username("john_investor")
                .email("john@example.com")
                .password(passwordEncoder.encode("john123"))
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("919876543211")
                .role(User.UserRole.INVESTOR)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .loginAttempts(0)
                .build(),

            User.builder()
                .username("jane_owner")
                .email("jane@example.com")
                .password(passwordEncoder.encode("jane123"))
                .firstName("Jane")
                .lastName("Smith")
                .phoneNumber("919876543212")
                .role(User.UserRole.PROPERTY_OWNER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .loginAttempts(0)
                .build()
        );

        userRepository.saveAll(users);
        log.info("Created {} users", users.size());
    }

    private void initializeProperties() {
        User owner = userRepository.findByUsername("jane_owner").orElse(null);
        if (owner == null) {
            log.error("Owner user not found!");
            return;
        }

        List<Property> properties = Arrays.asList(
            // Pune Properties (6)
            createProperty("Premium 3BHK in Koregaon Park", "Luxurious 3BHK apartment in prestigious Koregaon Park with modern amenities", new BigDecimal("15000000"), "Koregaon Park, Pune", "Pune", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("1800"), 3, 2, 2020, owner),
            createProperty("Spacious Villa in Baner", "Independent 4BHK villa with garden and parking in prime Baner location", new BigDecimal("25000000"), "Baner, Pune", "Pune", "Maharashtra", Property.PropertyType.VILLA, new BigDecimal("2500"), 4, 3, 2019, owner),
            createProperty("Modern 2BHK in Hinjewadi", "Contemporary 2BHK flat in IT hub Hinjewadi with excellent amenities", new BigDecimal("8500000"), "Hinjewadi, Pune", "Pune", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("1200"), 2, 2, 2021, owner),
            createProperty("Luxury Penthouse Wakad", "Exclusive penthouse with terrace garden in upscale Wakad area", new BigDecimal("35000000"), "Wakad, Pune", "Pune", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("3000"), 4, 4, 2022, owner),
            createProperty("Elegant 3BHK in Aundh", "Sophisticated 3BHK apartment in sought-after Aundh with premium facilities", new BigDecimal("18000000"), "Aundh, Pune", "Pune", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("1650"), 3, 3, 2021, owner),
            createProperty("Heritage Bungalow Pune Cantonment", "Beautiful colonial-style bungalow in prestigious Pune Cantonment area", new BigDecimal("42000000"), "Pune Cantonment, Pune", "Pune", "Maharashtra", Property.PropertyType.RESIDENTIAL, new BigDecimal("3500"), 5, 4, 2015, owner),

            // Mumbai Properties (6)
            createProperty("Sea View 4BHK in Bandra West", "Luxurious sea-facing apartment in premium Bandra West with stunning Arabian Sea views", new BigDecimal("65000000"), "Bandra West, Mumbai", "Mumbai", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("2200"), 4, 3, 2020, owner),
            createProperty("Skyscraper Penthouse Lower Parel", "Ultra-modern penthouse in iconic Lower Parel tower with panoramic city views", new BigDecimal("95000000"), "Lower Parel, Mumbai", "Mumbai", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("3800"), 4, 4, 2022, owner),
            createProperty("Compact 2BHK in Andheri East", "Well-designed 2BHK apartment near metro and airport in buzzing Andheri East", new BigDecimal("22000000"), "Andheri East, Mumbai", "Mumbai", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("950"), 2, 2, 2021, owner),
            createProperty("Heritage Apartment in Fort", "Charming heritage apartment in historic Fort area with colonial architecture", new BigDecimal("38000000"), "Fort, Mumbai", "Mumbai", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("1650"), 3, 2, 2018, owner),
            createProperty("Modern 3BHK in Powai", "Contemporary apartment in IT hub Powai with lake views and tech park proximity", new BigDecimal("28000000"), "Powai, Mumbai", "Mumbai", "Maharashtra", Property.PropertyType.APARTMENT, new BigDecimal("1450"), 3, 2, 2019, owner),
            createProperty("Luxury Villa in Juhu", "Exclusive independent villa near Juhu Beach with private garden and pool", new BigDecimal("120000000"), "Juhu, Mumbai", "Mumbai", "Maharashtra", Property.PropertyType.VILLA, new BigDecimal("4500"), 5, 5, 2021, owner),

            // Delhi Properties (6)
            createProperty("Elite Apartment in Connaught Place", "Premium 3BHK in central Delhi with excellent connectivity and amenities", new BigDecimal("28000000"), "Connaught Place, Delhi", "Delhi", "Delhi", Property.PropertyType.APARTMENT, new BigDecimal("1900"), 3, 2, 2019, owner),
            createProperty("Spacious Home in South Extension", "Large 4BHK independent house in upscale South Extension area", new BigDecimal("35000000"), "South Extension, Delhi", "Delhi", "Delhi", Property.PropertyType.RESIDENTIAL, new BigDecimal("2800"), 4, 3, 2016, owner),
            createProperty("Modern Condo in Dwarka", "Contemporary 2BHK flat in well-planned Dwarka with metro connectivity", new BigDecimal("11000000"), "Dwarka, Delhi", "Delhi", "Delhi", Property.PropertyType.APARTMENT, new BigDecimal("1300"), 2, 2, 2020, owner),
            createProperty("Luxury Penthouse in Vasant Kunj", "Exclusive penthouse with panoramic views in premium Vasant Kunj", new BigDecimal("55000000"), "Vasant Kunj, Delhi", "Delhi", "Delhi", Property.PropertyType.APARTMENT, new BigDecimal("3500"), 4, 4, 2022, owner),
            createProperty("Commercial Space CP", "Prime commercial office space in the heart of Connaught Place", new BigDecimal("40000000"), "Connaught Place, Delhi", "Delhi", "Delhi", Property.PropertyType.COMMERCIAL, new BigDecimal("2000"), 0, 2, 2018, owner),
            createProperty("Designer 3BHK in Greater Kailash", "Architect-designed luxury apartment in posh Greater Kailash with premium finishes", new BigDecimal("32000000"), "Greater Kailash, Delhi", "Delhi", "Delhi", Property.PropertyType.APARTMENT, new BigDecimal("2100"), 3, 3, 2021, owner),

            // Bangalore Properties (6)
            createProperty("IT Hub Apartment Whitefield", "Modern 3BHK apartment in IT corridor Whitefield with tech park proximity", new BigDecimal("16000000"), "Whitefield, Bangalore", "Bangalore", "Karnataka", Property.PropertyType.APARTMENT, new BigDecimal("1700"), 3, 2, 2021, owner),
            createProperty("Garden Villa in HSR Layout", "Beautiful villa with garden in family-friendly HSR Layout", new BigDecimal("32000000"), "HSR Layout, Bangalore", "Bangalore", "Karnataka", Property.PropertyType.VILLA, new BigDecimal("2600"), 4, 3, 2019, owner),
            createProperty("Startup Office in Koramangala", "Contemporary office space perfect for startups in vibrant Koramangala", new BigDecimal("22000000"), "Koramangala, Bangalore", "Bangalore", "Karnataka", Property.PropertyType.OFFICE, new BigDecimal("1500"), 0, 3, 2020, owner),
            createProperty("Luxury Flat in Brigade Road", "Premium 2BHK apartment in shopping district Brigade Road", new BigDecimal("18000000"), "Brigade Road, Bangalore", "Bangalore", "Karnataka", Property.PropertyType.APARTMENT, new BigDecimal("1400"), 2, 2, 2022, owner),
            createProperty("Tech Park Villa Electronic City", "Spacious villa near Electronic City tech parks with excellent connectivity", new BigDecimal("28000000"), "Electronic City, Bangalore", "Bangalore", "Karnataka", Property.PropertyType.VILLA, new BigDecimal("2400"), 4, 3, 2020, owner),
            createProperty("Premium 4BHK in Indiranagar", "Upscale 4BHK apartment in trendy Indiranagar with rooftop amenities", new BigDecimal("35000000"), "Indiranagar, Bangalore", "Bangalore", "Karnataka", Property.PropertyType.APARTMENT, new BigDecimal("2300"), 4, 3, 2021, owner)
        );

        propertyRepository.saveAll(properties);
        log.info("Created {} properties", properties.size());
    }

    private Property createProperty(String title, String description, BigDecimal price, String location,
                                  String city, String state, Property.PropertyType type, BigDecimal area,
                                  Integer bedrooms, Integer bathrooms, Integer yearBuilt, User owner) {

        String pincode = getPincodeForCity(city);

        return Property.builder()
            .title(title)
            .description(description)
            .price(price)
            .location(location)
            .city(city)
            .state(state)
            .pincode(pincode)
            .propertyType(type)
            .status(Property.PropertyStatus.AVAILABLE)
            .area(area)
            .areaUnit(Property.AreaUnit.SQFT)
            .bedrooms(bedrooms)
            .bathrooms(bathrooms)
            .yearBuilt(yearBuilt)
            .owner(owner)
            .listingDate(LocalDateTime.now())
            .viewCount(0L)
            .isFurnished(true)
            .hasGarden(type == Property.PropertyType.VILLA || type == Property.PropertyType.RESIDENTIAL)
            .hasPool(price.compareTo(new BigDecimal("30000000")) > 0)
            .hasGarage(true)
            .build();
    }

    private String getPincodeForCity(String city) {
        switch (city.toLowerCase()) {
            case "pune": return "411001";
            case "mumbai": return "400001";
            case "delhi": return "110001";
            case "bangalore": return "560001";
            default: return "000000";
        }
    }
}