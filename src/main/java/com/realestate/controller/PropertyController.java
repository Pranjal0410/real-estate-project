package com.realestate.controller;

import com.realestate.model.dto.ApiResponse;
import com.realestate.model.dto.PropertyDTO;
import com.realestate.model.dto.PropertySearchDTO;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyDTO>>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.findAll();
        return ResponseEntity.ok(ApiResponse.success(properties, "Properties fetched successfully"));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PropertyDTO>> getPropertiesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(propertyService.findAllPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyDTO>> getPropertyById(
            @PathVariable @Min(1) Long id) {
        PropertyDTO property = propertyService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(property, "Property fetched successfully"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public ResponseEntity<ApiResponse<PropertyDTO>> createProperty(
            @Valid @RequestBody PropertyDTO propertyDTO) {
        log.info("Creating new property: {}", propertyDTO.getTitle());
        PropertyDTO created = propertyService.create(propertyDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(created, "Property created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public ResponseEntity<ApiResponse<PropertyDTO>> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyDTO propertyDTO) {
        log.info("Updating property with id: {}", id);
        PropertyDTO updated = propertyService.update(id, propertyDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Property updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProperty(@PathVariable Long id) {
        log.info("Deleting property with id: {}", id);
        propertyService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Property deleted successfully"));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<PropertyDTO>>> searchProperties(
            @RequestBody PropertySearchDTO searchDTO) {
        List<PropertyDTO> properties = propertyService.searchProperties(searchDTO);
        return ResponseEntity.ok(ApiResponse.success(properties, "Search completed"));
    }

    @PostMapping("/search/async")
    public CompletableFuture<ResponseEntity<ApiResponse<List<PropertyDTO>>>> searchPropertiesAsync(
            @RequestBody PropertySearchDTO searchDTO) {
        return propertyService.searchPropertiesAsync(searchDTO)
            .thenApply(properties -> ResponseEntity.ok(
                ApiResponse.success(properties, "Async search completed")));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<ApiResponse<List<PropertyDTO>>> getPropertiesByLocation(
            @PathVariable String location) {
        List<PropertyDTO> properties = propertyService.findByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(properties, "Properties fetched by location"));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<PropertyDTO>>> getPropertiesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<PropertyDTO> properties = propertyService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(properties, "Properties fetched by price range"));
    }

    @GetMapping("/grouped-by-type")
    public ResponseEntity<ApiResponse<Map<String, List<PropertyDTO>>>> getPropertiesGroupedByType() {
        Map<String, List<PropertyDTO>> grouped = propertyService.groupPropertiesByType();
        return ResponseEntity.ok(ApiResponse.success(grouped, "Properties grouped by type"));
    }

    @GetMapping("/average-price-by-location")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getAveragePriceByLocation() {
        Map<String, Double> averagePrices = propertyService.getAveragePriceByLocation();
        return ResponseEntity.ok(ApiResponse.success(averagePrices, "Average prices by location"));
    }

    @GetMapping("/top/{limit}")
    public ResponseEntity<ApiResponse<List<PropertyDTO>>> getTopProperties(
            @PathVariable @Min(1) int limit) {
        List<PropertyDTO> properties = propertyService.getTopProperties(limit);
        return ResponseEntity.ok(ApiResponse.success(properties, "Top properties fetched"));
    }

    @GetMapping("/cheapest")
    public ResponseEntity<ApiResponse<PropertyDTO>> getCheapestProperty() {
        return propertyService.findCheapestProperty()
            .map(property -> ResponseEntity.ok(
                ApiResponse.success(property, "Cheapest property found")))
            .orElse(ResponseEntity.ok(
                ApiResponse.<PropertyDTO>error("No properties found")));
    }

    @GetMapping("/most-expensive")
    public ResponseEntity<ApiResponse<PropertyDTO>> getMostExpensiveProperty() {
        return propertyService.findMostExpensiveProperty()
            .map(property -> ResponseEntity.ok(
                ApiResponse.success(property, "Most expensive property found")))
            .orElse(ResponseEntity.ok(
                ApiResponse.<PropertyDTO>error("No properties found")));
    }

    @GetMapping("/count-by-type")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getPropertyCountByType() {
        Map<String, Long> counts = propertyService.getPropertyCountByType();
        return ResponseEntity.ok(ApiResponse.success(counts, "Property counts by type"));
    }

    @GetMapping("/total-value")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalValue() {
        BigDecimal totalValue = propertyService.calculateTotalValue();
        return ResponseEntity.ok(ApiResponse.success(totalValue, "Total property value calculated"));
    }
}