package com.realestate.service;

import com.realestate.exception.ResourceNotFoundException;
import com.realestate.model.dto.PropertyDTO;
import com.realestate.model.dto.PropertySearchDTO;
import com.realestate.model.entity.Property;
import com.realestate.repository.PropertyRepository;
import com.realestate.util.PropertyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;
    
    @Mock
    private PropertyMapper propertyMapper;
    
    @InjectMocks
    private PropertyService propertyService;
    
    private Property testProperty;
    private PropertyDTO testPropertyDTO;
    
    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setId(1L);
        testProperty.setTitle("Test Property");
        testProperty.setDescription("Test Description");
        testProperty.setPrice(new BigDecimal("500000"));
        testProperty.setLocation("New York");
        testProperty.setPropertyType("Apartment");
        testProperty.setArea(1200.0);
        testProperty.setBedrooms(3);
        testProperty.setBathrooms(2);
        
        testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setId(1L);
        testPropertyDTO.setTitle("Test Property");
        testPropertyDTO.setDescription("Test Description");
        testPropertyDTO.setPrice(new BigDecimal("500000"));
        testPropertyDTO.setLocation("New York");
        testPropertyDTO.setPropertyType("Apartment");
        testPropertyDTO.setArea(1200.0);
        testPropertyDTO.setBedrooms(3);
        testPropertyDTO.setBathrooms(2);
    }
    
    @Test
    void testFindById_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(propertyMapper.toDTO(testProperty)).thenReturn(testPropertyDTO);
        
        PropertyDTO result = propertyService.findById(1L);
        
        assertNotNull(result);
        assertEquals("Test Property", result.getTitle());
        verify(propertyRepository).findById(1L);
        verify(propertyMapper).toDTO(testProperty);
    }
    
    @Test
    void testFindById_NotFound() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.findById(99L);
        });
    }
    
    @Test
    void testFindAll() {
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findAll()).thenReturn(properties);
        when(propertyMapper.toDTO(any())).thenReturn(testPropertyDTO);
        
        List<PropertyDTO> result = propertyService.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
    
    @Test
    void testCreate_Success() {
        when(propertyMapper.toEntity(testPropertyDTO)).thenReturn(testProperty);
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(propertyMapper.toDTO(testProperty)).thenReturn(testPropertyDTO);
        
        PropertyDTO result = propertyService.create(testPropertyDTO);
        
        assertNotNull(result);
        assertEquals("Test Property", result.getTitle());
        verify(propertyRepository).save(any(Property.class));
    }
    
    @Test
    void testUpdate_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(propertyMapper.toDTO(testProperty)).thenReturn(testPropertyDTO);
        
        PropertyDTO result = propertyService.update(1L, testPropertyDTO);
        
        assertNotNull(result);
        verify(propertyRepository).save(testProperty);
    }
    
    @Test
    void testDelete_Success() {
        when(propertyRepository.existsById(1L)).thenReturn(true);
        
        assertDoesNotThrow(() -> propertyService.delete(1L));
        
        verify(propertyRepository).deleteById(1L);
    }
    
    @Test
    void testDelete_NotFound() {
        when(propertyRepository.existsById(99L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.delete(99L);
        });
    }
    
    @Test
    void testSearchProperties() {
        PropertySearchDTO searchDTO = new PropertySearchDTO();
        searchDTO.setLocation("New York");
        searchDTO.setMinPrice(new BigDecimal("400000"));
        searchDTO.setMaxPrice(new BigDecimal("600000"));
        
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findAll()).thenReturn(properties);
        when(propertyMapper.toDTO(any())).thenReturn(testPropertyDTO);
        
        List<PropertyDTO> result = propertyService.searchProperties(searchDTO);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void testFindByLocation() {
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findByLocationContainingIgnoreCase("New York"))
            .thenReturn(properties);
        when(propertyMapper.toDTO(any())).thenReturn(testPropertyDTO);
        
        List<PropertyDTO> result = propertyService.findByLocation("New York");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("New York", result.get(0).getLocation());
    }
    
    @Test
    void testFindByPriceRange() {
        BigDecimal minPrice = new BigDecimal("400000");
        BigDecimal maxPrice = new BigDecimal("600000");
        
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findByPriceBetween(minPrice, maxPrice))
            .thenReturn(properties);
        when(propertyMapper.toDTO(any())).thenReturn(testPropertyDTO);
        
        List<PropertyDTO> result = propertyService.findByPriceRange(minPrice, maxPrice);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void testCalculateTotalValue() {
        Property property2 = new Property();
        property2.setPrice(new BigDecimal("300000"));
        
        List<Property> properties = Arrays.asList(testProperty, property2);
        when(propertyRepository.findAll()).thenReturn(properties);
        
        BigDecimal totalValue = propertyService.calculateTotalValue();
        
        assertNotNull(totalValue);
        assertEquals(new BigDecimal("800000"), totalValue);
    }
    
    @Test
    void testFindCheapestProperty() {
        Property cheapProperty = new Property();
        cheapProperty.setPrice(new BigDecimal("200000"));
        
        List<Property> properties = Arrays.asList(testProperty, cheapProperty);
        when(propertyRepository.findAll()).thenReturn(properties);
        when(propertyMapper.toDTO(cheapProperty)).thenReturn(testPropertyDTO);
        
        Optional<PropertyDTO> result = propertyService.findCheapestProperty();
        
        assertTrue(result.isPresent());
    }
    
    @Test
    void testGetPropertyCountByType() {
        Property property2 = new Property();
        property2.setPropertyType("House");
        
        List<Property> properties = Arrays.asList(testProperty, property2);
        when(propertyRepository.findAll()).thenReturn(properties);
        
        Map<String, Long> counts = propertyService.getPropertyCountByType();
        
        assertNotNull(counts);
        assertEquals(1L, counts.get("Apartment"));
        assertEquals(1L, counts.get("House"));
    }
}