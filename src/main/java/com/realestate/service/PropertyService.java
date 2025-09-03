package com.realestate.service;

import com.realestate.exception.ResourceNotFoundException;
import com.realestate.exception.BusinessException;
import com.realestate.model.dto.PropertyDTO;
import com.realestate.model.dto.PropertySearchDTO;
import com.realestate.model.entity.Property;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.PropertyJdbcRepository;
import com.realestate.util.PropertyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyJdbcRepository propertyJdbcRepository;
    private final PropertyMapper propertyMapper;
    
    private final Map<Long, Property> propertyCache = new ConcurrentHashMap<>();
    
    @Cacheable(value = "properties", key = "#id")
    public PropertyDTO findById(Long id) {
        log.info("Fetching property with id: {}", id);
        
        Optional<Property> property = Optional.ofNullable(propertyCache.get(id))
            .or(() -> propertyRepository.findById(id))
            .map(p -> {
                propertyCache.put(id, p);
                return p;
            });
            
        return property.map(propertyMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    }
    
    @Cacheable(value = "properties", key = "'all'")
    public List<PropertyDTO> findAll() {
        log.info("Fetching all properties");
        
        List<Property> properties = propertyRepository.findAll();
        
        List<PropertyDTO> propertyDTOs = properties.stream()
            .filter(Objects::nonNull)
            .map(propertyMapper::toDTO)
            .sorted(Comparator.comparing(PropertyDTO::getPrice).reversed())
            .collect(Collectors.toList());
            
        log.info("Found {} properties", propertyDTOs.size());
        return propertyDTOs;
    }
    
    public Page<PropertyDTO> findAllPaginated(Pageable pageable) {
        return propertyRepository.findAll(pageable)
            .map(propertyMapper::toDTO);
    }
    
    public List<PropertyDTO> searchProperties(PropertySearchDTO searchDTO) {
        log.info("Searching properties with criteria: {}", searchDTO);
        
        List<Property> allProperties = propertyRepository.findAll();
        
        Predicate<Property> locationPredicate = property -> 
            searchDTO.getLocation() == null || 
            property.getLocation().toLowerCase().contains(searchDTO.getLocation().toLowerCase());
            
        Predicate<Property> minPricePredicate = property -> 
            searchDTO.getMinPrice() == null || 
            property.getPrice().compareTo(searchDTO.getMinPrice()) >= 0;
            
        Predicate<Property> maxPricePredicate = property -> 
            searchDTO.getMaxPrice() == null || 
            property.getPrice().compareTo(searchDTO.getMaxPrice()) <= 0;
            
        Predicate<Property> typePredicate = property -> 
            searchDTO.getPropertyType() == null || 
            property.getPropertyType().equals(searchDTO.getPropertyType());
            
        Predicate<Property> bedroomsPredicate = property -> 
            searchDTO.getMinBedrooms() == null || 
            property.getBedrooms() >= searchDTO.getMinBedrooms();
            
        Predicate<Property> areaPredicate = property -> 
            searchDTO.getMinArea() == null || 
            property.getArea().compareTo(searchDTO.getMinArea()) >= 0;
            
        return allProperties.stream()
            .filter(locationPredicate
                .and(minPricePredicate)
                .and(maxPricePredicate)
                .and(typePredicate)
                .and(bedroomsPredicate)
                .and(areaPredicate))
            .map(propertyMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDTO create(PropertyDTO propertyDTO) {
        log.info("Creating new property: {}", propertyDTO);
        
        if (propertyDTO.getPrice() == null || propertyDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Property price must be greater than 0");
        }
        
        Property property = propertyMapper.toEntity(propertyDTO);
        // Dates are handled by @CreatedDate and @LastModifiedDate annotations
        
        Property savedProperty = propertyRepository.save(property);
        propertyCache.put(savedProperty.getId(), savedProperty);
        
        log.info("Property created successfully with id: {}", savedProperty.getId());
        return propertyMapper.toDTO(savedProperty);
    }
    
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDTO update(Long id, PropertyDTO propertyDTO) {
        log.info("Updating property with id: {}", id);
        
        Property existingProperty = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
            
        updatePropertyFields(existingProperty, propertyDTO);
        // Updated date is handled by @LastModifiedDate annotation
        
        Property updatedProperty = propertyRepository.save(existingProperty);
        propertyCache.put(id, updatedProperty);
        
        log.info("Property updated successfully");
        return propertyMapper.toDTO(updatedProperty);
    }
    
    @CacheEvict(value = "properties", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting property with id: {}", id);
        
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property not found with id: " + id);
        }
        
        propertyRepository.deleteById(id);
        propertyCache.remove(id);
        
        log.info("Property deleted successfully");
    }
    
    public List<PropertyDTO> findByLocation(String location) {
        return propertyRepository.findByLocationContainingIgnoreCase(location).stream()
            .map(propertyMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<PropertyDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyRepository.findByPriceBetween(minPrice, maxPrice).stream()
            .map(propertyMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public Map<String, List<PropertyDTO>> groupPropertiesByType() {
        List<Property> properties = propertyRepository.findAll();
        
        return properties.stream()
            .collect(Collectors.groupingBy(
                property -> property.getPropertyType().name(),
                Collectors.mapping(propertyMapper::toDTO, Collectors.toList())
            ));
    }
    
    public Map<String, Double> getAveragePriceByLocation() {
        List<Property> properties = propertyRepository.findAll();
        
        return properties.stream()
            .collect(Collectors.groupingBy(
                Property::getLocation,
                Collectors.averagingDouble(p -> p.getPrice().doubleValue())
            ));
    }
    
    public List<PropertyDTO> getTopProperties(int limit) {
        List<Property> properties = propertyRepository.findAll();
        
        return properties.stream()
            .sorted(Comparator.comparing(Property::getPrice).reversed())
            .limit(limit)
            .map(propertyMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public CompletableFuture<List<PropertyDTO>> searchPropertiesAsync(PropertySearchDTO searchDTO) {
        return CompletableFuture.supplyAsync(() -> searchProperties(searchDTO));
    }
    
    public Optional<PropertyDTO> findCheapestProperty() {
        List<Property> properties = propertyRepository.findAll();
        
        return properties.stream()
            .min(Comparator.comparing(Property::getPrice))
            .map(propertyMapper::toDTO);
    }
    
    public Optional<PropertyDTO> findMostExpensiveProperty() {
        List<Property> properties = propertyRepository.findAll();
        
        return properties.stream()
            .max(Comparator.comparing(Property::getPrice))
            .map(propertyMapper::toDTO);
    }
    
    public Map<String, Long> getPropertyCountByType() {
        List<Property> properties = propertyRepository.findAll();
        
        return properties.stream()
            .collect(Collectors.groupingBy(
                property -> property.getPropertyType().name(),
                Collectors.counting()
            ));
    }
    
    private void updatePropertyFields(Property property, PropertyDTO dto) {
        Optional.ofNullable(dto.getTitle()).ifPresent(property::setTitle);
        Optional.ofNullable(dto.getDescription()).ifPresent(property::setDescription);
        Optional.ofNullable(dto.getPrice()).ifPresent(property::setPrice);
        Optional.ofNullable(dto.getLocation()).ifPresent(property::setLocation);
        Optional.ofNullable(dto.getPropertyType())
            .ifPresent(type -> property.setPropertyType(Property.PropertyType.valueOf(type.name())));
        Optional.ofNullable(dto.getArea()).ifPresent(property::setArea);
        Optional.ofNullable(dto.getBedrooms()).ifPresent(property::setBedrooms);
        Optional.ofNullable(dto.getBathrooms()).ifPresent(property::setBathrooms);
        Optional.ofNullable(dto.getYearBuilt()).ifPresent(property::setYearBuilt);
        // ImageUrls are handled as a Set in the entity
        Optional.ofNullable(dto.getImageUrls()).ifPresent(property::setImageUrls);
    }
    
    public List<PropertyDTO> filterPropertiesWithHigherOrderFunction(
            Function<Property, Boolean> filterFunction) {
        
        return propertyRepository.findAll().stream()
            .filter(filterFunction::apply)
            .map(propertyMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public <R> List<R> transformProperties(Function<Property, R> transformer) {
        return propertyRepository.findAll().stream()
            .map(transformer)
            .collect(Collectors.toList());
    }
    
    public BigDecimal calculateTotalValue() {
        return propertyRepository.findAll().stream()
            .map(Property::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public List<PropertyDTO> findPropertiesUsingJDBC(String location, BigDecimal minPrice) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("city", location);
        criteria.put("minPrice", minPrice);
        
        return propertyJdbcRepository.executeComplexPropertySearch(criteria)
            .stream()
            .map(propertyMapper::toDTO)
            .collect(Collectors.toList());
    }
}