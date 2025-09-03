package com.realestate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.model.dto.PropertyDTO;
import com.realestate.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private PropertyService propertyService;
    
    private PropertyDTO testPropertyDTO;
    
    @BeforeEach
    void setUp() {
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
    @WithMockUser
    void testGetAllProperties() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(testPropertyDTO);
        when(propertyService.findAll()).thenReturn(properties);
        
        mockMvc.perform(get("/api/properties"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].title", is("Test Property")));
        
        verify(propertyService).findAll();
    }
    
    @Test
    @WithMockUser
    void testGetPropertyById() throws Exception {
        when(propertyService.findById(1L)).thenReturn(testPropertyDTO);
        
        mockMvc.perform(get("/api/properties/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.id", is(1)))
            .andExpect(jsonPath("$.data.title", is("Test Property")));
        
        verify(propertyService).findById(1L);
    }
    
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateProperty() throws Exception {
        when(propertyService.create(any(PropertyDTO.class))).thenReturn(testPropertyDTO);
        
        mockMvc.perform(post("/api/properties")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPropertyDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.title", is("Test Property")));
        
        verify(propertyService).create(any(PropertyDTO.class));
    }
    
    @Test
    @WithMockUser(roles = {"USER"})
    void testCreateProperty_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/properties")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPropertyDTO)))
            .andExpect(status().isForbidden());
        
        verify(propertyService, never()).create(any());
    }
    
    @Test
    @WithMockUser(roles = {"PROPERTY_OWNER"})
    void testUpdateProperty() throws Exception {
        when(propertyService.update(anyLong(), any(PropertyDTO.class))).thenReturn(testPropertyDTO);
        
        mockMvc.perform(put("/api/properties/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPropertyDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.title", is("Test Property")));
        
        verify(propertyService).update(eq(1L), any(PropertyDTO.class));
    }
    
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteProperty() throws Exception {
        doNothing().when(propertyService).delete(1L);
        
        mockMvc.perform(delete("/api/properties/1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)));
        
        verify(propertyService).delete(1L);
    }
    
    @Test
    @WithMockUser
    void testGetPropertiesByLocation() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(testPropertyDTO);
        when(propertyService.findByLocation("New York")).thenReturn(properties);
        
        mockMvc.perform(get("/api/properties/location/New York"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(1)));
        
        verify(propertyService).findByLocation("New York");
    }
    
    @Test
    @WithMockUser
    void testGetPropertiesByPriceRange() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(testPropertyDTO);
        BigDecimal minPrice = new BigDecimal("400000");
        BigDecimal maxPrice = new BigDecimal("600000");
        
        when(propertyService.findByPriceRange(minPrice, maxPrice)).thenReturn(properties);
        
        mockMvc.perform(get("/api/properties/price-range")
                .param("minPrice", "400000")
                .param("maxPrice", "600000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(1)));
        
        verify(propertyService).findByPriceRange(minPrice, maxPrice);
    }
    
    @Test
    @WithMockUser
    void testGetTotalValue() throws Exception {
        BigDecimal totalValue = new BigDecimal("1500000");
        when(propertyService.calculateTotalValue()).thenReturn(totalValue);
        
        mockMvc.perform(get("/api/properties/total-value"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", is(1500000)));
        
        verify(propertyService).calculateTotalValue();
    }
}