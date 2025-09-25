package com.mochafund.workspaceservice.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {

    @NotBlank(message = "Tag name must be provided")
    @Size(min = 1, max = 100, message = "Tag name must be between 1 and 100 characters")
    private String name;

    private String description;
    private boolean isIncome;
    private boolean excludeFromBudget;
    private boolean excludeFromTotals;
}
