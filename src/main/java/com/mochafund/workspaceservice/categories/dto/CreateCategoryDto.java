package com.mochafund.workspaceservice.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {

    @NotBlank(message = "Category name must be provided")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String name;

    private String description;
    private Boolean income;
    private Boolean excludeFromBudget;
    private Boolean excludeFromTotals;
    private UUID parentId;
}
