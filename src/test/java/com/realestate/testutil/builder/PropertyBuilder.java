package com.realestate.testutil.builder;

import com.realestate.model.entity.Property;
import com.realestate.model.entity.User;

import java.math.BigDecimal;

public class PropertyBuilder {

    private Long id = 1L;
    private String title = "Test Property";
    private BigDecimal price = new BigDecimal("500000.00");
    private String location = "Test City";
    private String city = "Test City";
    private String state = "Test State";
    private Property.PropertyType propertyType = Property.PropertyType.RESIDENTIAL;
    private BigDecimal area = new BigDecimal("1500.00");
    private Integer bedrooms = 3;
    private Integer bathrooms = 2;
    private Property.PropertyStatus status = Property.PropertyStatus.AVAILABLE;
    private User owner;

    public static PropertyBuilder aProperty() {
        return new PropertyBuilder();
    }

    public PropertyBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PropertyBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PropertyBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public PropertyBuilder withPrice(double price) {
        this.price = new BigDecimal(price);
        return this;
    }

    public PropertyBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public PropertyBuilder withPropertyType(Property.PropertyType type) {
        this.propertyType = type;
        return this;
    }

    public PropertyBuilder withArea(BigDecimal area) {
        this.area = area;
        return this;
    }

    public PropertyBuilder withBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
        return this;
    }

    public PropertyBuilder withBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
        return this;
    }

    public PropertyBuilder withStatus(Property.PropertyStatus status) {
        this.status = status;
        return this;
    }

    public PropertyBuilder withOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public PropertyBuilder residential() {
        this.propertyType = Property.PropertyType.RESIDENTIAL;
        return this;
    }

    public PropertyBuilder commercial() {
        this.propertyType = Property.PropertyType.COMMERCIAL;
        return this;
    }

    public PropertyBuilder sold() {
        this.status = Property.PropertyStatus.SOLD;
        return this;
    }

    public Property build() {
        if (owner == null) {
            owner = UserBuilder.aUser().build();
        }

        Property property = Property.builder()
                .title(title)
                .price(price)
                .location(location)
                .city(city)
                .state(state)
                .propertyType(propertyType)
                .area(area)
                .bedrooms(bedrooms)
                .bathrooms(bathrooms)
                .status(status)
                .owner(owner)
                .build();
        property.setId(id);
        return property;
    }
}
