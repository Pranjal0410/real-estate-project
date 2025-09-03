package com.realestate.util;

import com.realestate.model.dto.PropertyDTO;
import com.realestate.model.entity.Property;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PropertyMapper implements Function<Property, PropertyDTO> {

    public PropertyDTO toDTO(Property property) {
        if (property == null) {
            return null;
        }
        
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setPrice(property.getPrice());
        dto.setLocation(property.getLocation());
        dto.setCity(property.getCity());
        dto.setState(property.getState());
        dto.setPincode(property.getPincode());
        dto.setPropertyType(PropertyDTO.PropertyType.valueOf(property.getPropertyType().name()));
        dto.setStatus(PropertyDTO.PropertyStatus.valueOf(property.getStatus().name()));
        dto.setArea(property.getArea());
        dto.setAreaUnit(PropertyDTO.AreaUnit.valueOf(property.getAreaUnit().name()));
        dto.setBedrooms(property.getBedrooms());
        dto.setBathrooms(property.getBathrooms());
        dto.setParkingSpaces(property.getParkingSpaces());
        dto.setYearBuilt(property.getYearBuilt());
        dto.setIsFurnished(property.getIsFurnished());
        dto.setHasGarden(property.getHasGarden());
        dto.setHasPool(property.getHasPool());
        dto.setHasGarage(property.getHasGarage());
        dto.setAmenities(property.getAmenities());
        dto.setImageUrls(property.getImageUrls());
        dto.setMonthlyRental(property.getMonthlyRental());
        dto.setAnnualMaintenance(property.getAnnualMaintenance());
        dto.setPropertyTax(property.getPropertyTax());
        dto.setHoaFees(property.getHoaFees());
        dto.setInsuranceCost(property.getInsuranceCost());
        dto.setListingDate(property.getListingDate());
        dto.setSoldDate(property.getSoldDate());
        dto.setViewCount(property.getViewCount());
        dto.setLatitude(property.getLatitude());
        dto.setLongitude(property.getLongitude());
        if (property.getOwner() != null) {
            dto.setOwnerId(property.getOwner().getId());
            dto.setOwnerName(property.getOwner().getFirstName() + " " + property.getOwner().getLastName());
            dto.setOwnerPhone(property.getOwner().getPhoneNumber());
        }
        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());
        
        return dto;
    }
    
    public Property toEntity(PropertyDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Property property = new Property();
        property.setId(dto.getId());
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setPrice(dto.getPrice());
        property.setLocation(dto.getLocation());
        property.setCity(dto.getCity());
        property.setState(dto.getState());
        property.setPincode(dto.getPincode());
        if (dto.getPropertyType() != null) {
            property.setPropertyType(Property.PropertyType.valueOf(dto.getPropertyType().name()));
        }
        if (dto.getStatus() != null) {
            property.setStatus(Property.PropertyStatus.valueOf(dto.getStatus().name()));
        }
        property.setArea(dto.getArea());
        if (dto.getAreaUnit() != null) {
            property.setAreaUnit(Property.AreaUnit.valueOf(dto.getAreaUnit().name()));
        }
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setParkingSpaces(dto.getParkingSpaces());
        property.setYearBuilt(dto.getYearBuilt());
        property.setIsFurnished(dto.getIsFurnished());
        property.setHasGarden(dto.getHasGarden());
        property.setHasPool(dto.getHasPool());
        property.setHasGarage(dto.getHasGarage());
        property.setAmenities(dto.getAmenities());
        property.setImageUrls(dto.getImageUrls());
        property.setMonthlyRental(dto.getMonthlyRental());
        property.setAnnualMaintenance(dto.getAnnualMaintenance());
        property.setPropertyTax(dto.getPropertyTax());
        property.setHoaFees(dto.getHoaFees());
        property.setInsuranceCost(dto.getInsuranceCost());
        property.setListingDate(dto.getListingDate());
        property.setSoldDate(dto.getSoldDate());
        property.setViewCount(dto.getViewCount());
        property.setLatitude(dto.getLatitude());
        property.setLongitude(dto.getLongitude());
        
        return property;
    }

    @Override
    public PropertyDTO apply(Property property) {
        return toDTO(property);
    }
}