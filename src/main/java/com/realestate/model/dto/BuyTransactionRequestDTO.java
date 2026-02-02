package com.realestate.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyTransactionRequestDTO {

    @NotNull(message = "Portfolio ID is required")
    private Long portfolioId;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.00000001", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    private String idempotencyKey;
}
