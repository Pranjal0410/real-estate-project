package com.realestate.controller;

import com.realestate.model.dto.UserDTO;
import com.realestate.service.PropertyService;
import com.realestate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final PropertyService propertyService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("properties", propertyService.getTopProperties(9));
        model.addAttribute("propertyCount", propertyService.findAll().size());
        model.addAttribute("userCount", userService.getUserCount());
        return "index";
    }

    @GetMapping("/properties")
    public String properties(Model model,
                           @RequestParam(required = false) String location,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String minPrice,
                           @RequestParam(required = false) String maxPrice,
                           @RequestParam(required = false) String bedrooms,
                           @RequestParam(required = false) String budget,
                           @RequestParam(required = false) Boolean featured) {

        try {
            // Check if any filters are applied
            boolean hasFilters = (location != null && !location.isEmpty()) ||
                               (type != null && !type.isEmpty()) ||
                               (minPrice != null && !minPrice.isEmpty()) ||
                               (maxPrice != null && !maxPrice.isEmpty()) ||
                               (bedrooms != null && !bedrooms.isEmpty()) ||
                               (budget != null && !budget.isEmpty());

            if (hasFilters) {
                log.info("Searching properties with filters - location: {}, type: {}, minPrice: {}, maxPrice: {}, bedrooms: {}, budget: {}",
                        location, type, minPrice, maxPrice, bedrooms, budget);

                // Build search DTO
                var searchDTO = buildSearchDTO(location, type, minPrice, maxPrice, bedrooms, budget);
                model.addAttribute("properties", propertyService.searchProperties(searchDTO));
            } else if (featured != null && featured) {
                log.info("Fetching featured properties");
                model.addAttribute("properties", propertyService.getTopProperties(6));
            } else {
                log.info("Fetching all properties");
                model.addAttribute("properties", propertyService.findAll());
            }

            // Add model attributes for form state
            model.addAttribute("propertyTypes", propertyService.getPropertyCountByType());
            model.addAttribute("selectedLocation", location);
            model.addAttribute("selectedType", type);
            model.addAttribute("selectedMinPrice", minPrice);
            model.addAttribute("selectedMaxPrice", maxPrice);
            model.addAttribute("selectedBedrooms", bedrooms);
            model.addAttribute("selectedBudget", budget);
            model.addAttribute("hasFilters", hasFilters);

            return "properties";
        } catch (Exception e) {
            log.error("Error loading properties: {}", e.getMessage());
            model.addAttribute("properties", propertyService.findAll());
            model.addAttribute("error", "Error loading properties. Showing all available properties.");
            return "properties";
        }
    }

    private com.realestate.model.dto.PropertySearchDTO buildSearchDTO(String location, String type,
                                                                     String minPrice, String maxPrice,
                                                                     String bedrooms, String budget) {
        var searchDTO = com.realestate.model.dto.PropertySearchDTO.builder();

        // Location filter
        if (location != null && !location.isEmpty()) {
            searchDTO.location(location);
        }

        // Property type filter
        if (type != null && !type.isEmpty()) {
            try {
                searchDTO.propertyType(com.realestate.model.dto.PropertySearchDTO.PropertyType.valueOf(type.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid property type: {}", type);
            }
        }

        // Price range filters
        if (minPrice != null && !minPrice.isEmpty()) {
            try {
                searchDTO.minPrice(new java.math.BigDecimal(minPrice));
            } catch (NumberFormatException e) {
                log.warn("Invalid min price: {}", minPrice);
            }
        }

        if (maxPrice != null && !maxPrice.isEmpty()) {
            try {
                searchDTO.maxPrice(new java.math.BigDecimal(maxPrice));
            } catch (NumberFormatException e) {
                log.warn("Invalid max price: {}", maxPrice);
            }
        }

        // Budget filter (parse range like "0-2500000")
        if (budget != null && !budget.isEmpty() && budget.contains("-")) {
            try {
                String[] range = budget.split("-");
                if (range.length == 2) {
                    searchDTO.minPrice(new java.math.BigDecimal(range[0]));
                    searchDTO.maxPrice(new java.math.BigDecimal(range[1]));
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid budget range: {}", budget);
            }
        }

        // Bedrooms filter
        if (bedrooms != null && !bedrooms.isEmpty()) {
            try {
                searchDTO.minBedrooms(Integer.parseInt(bedrooms));
            } catch (NumberFormatException e) {
                log.warn("Invalid bedrooms: {}", bedrooms);
            }
        }

        return searchDTO.build();
    }

    @GetMapping("/property/{id}")
    public String propertyDetails(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.findById(id));
        return "property-details";
    }

    @GetMapping("/calculator")
    public String calculator(Model model) {
        model.addAttribute("properties", propertyService.findAll());
        return "calculator";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Try to get current user, but don't fail if we can't
            try {
                UserDTO currentUser = userService.getCurrentUser();
                model.addAttribute("user", currentUser);
                log.debug("Dashboard loaded for user: {}", currentUser.getUsername());
            } catch (Exception e) {
                log.warn("Could not get current user for dashboard: {}", e.getMessage());
                // Continue without user data - dashboard will show generic content
            }

            model.addAttribute("properties", propertyService.findAll());
            model.addAttribute("stats", propertyService.getPropertyCountByType());
            return "dashboard";
        } catch (Exception e) {
            log.error("Error loading dashboard: {}", e.getMessage());
            return "redirect:/login?error=dashboard";
        }
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        if (!userService.isCurrentUserAdmin()) {
            return "redirect:/dashboard";
        }
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("stats", propertyService.getPropertyCountByType());
        return "admin";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }
    
    @GetMapping("/propeties")
    public String propertiesTypo() {
        return "redirect:/properties";
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            model.addAttribute("user", userService.getCurrentUser());
            return "profile";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
    
    @GetMapping("/favorites")
    public String favorites(Model model) {
        try {
            model.addAttribute("user", userService.getCurrentUser());
            // Add favorites logic here when implemented
            model.addAttribute("properties", propertyService.findAll());
            return "favorites";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
    
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }
    
    @GetMapping("/sell/property")
    public String sellProperty(Model model) {
        try {
            model.addAttribute("user", userService.getCurrentUser());
            return "sell-property";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
    
    @GetMapping("/shortlist")
    public String shortlist(Model model) {
        try {
            model.addAttribute("user", userService.getCurrentUser());
            return "shortlist";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/emi-calculator")
    public String emiCalculator() {
        return "emi-calculator";
    }
}