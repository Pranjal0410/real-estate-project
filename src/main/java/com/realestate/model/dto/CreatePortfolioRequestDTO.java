package com.realestate.model.dto;

import com.realestate.model.entity.Portfolio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePortfolioRequestDTO {

    @NotBlank(message = "Portfolio name is required")
    @Size(max = 100, message = "Portfolio name must be at most 100 characters")
    private String portfolioName;

    private Portfolio.RiskProfile riskProfile;
}
