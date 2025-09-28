package com.mochafund.workspaceservice.category.dto;

import com.mochafund.workspaceservice.category.entity.Category;
import com.mochafund.workspaceservice.category.enums.CategoryStatus;
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
    private Boolean isIncome;
    private Boolean excludeFromBudget;
    private Boolean excludeFromTotals;
    private UUID parentId;

    public static Category fromDto(CreateCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .isIncome(Boolean.TRUE.equals(categoryDto.getIsIncome()))
                .excludeFromBudget(Boolean.TRUE.equals(categoryDto.getExcludeFromBudget()))
                .excludeFromTotals(Boolean.TRUE.equals(categoryDto.getExcludeFromTotals()))
                .status(CategoryStatus.ACTIVE)
                .build();
    }
}
