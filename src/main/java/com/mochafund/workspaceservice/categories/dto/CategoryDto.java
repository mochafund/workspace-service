package com.mochafund.workspaceservice.categories.dto;

import com.mochafund.workspaceservice.categories.entity.Category;
import com.mochafund.workspaceservice.categories.enums.CategoryStatus;
import com.mochafund.workspaceservice.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CategoryDto  extends BaseDto {

    private UUID workspaceId;
    private UUID createdBy;
    private String name;
    private String description;
    private CategoryStatus status;
    private boolean isIncome;
    private boolean excludeFromBudget;
    private boolean excludeFromTotals;

    public static CategoryDto fromEntity(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .workspaceId(category.getWorkspaceId())
                .createdBy(category.getCreatedBy())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .isIncome(category.isIncome())
                .excludeFromBudget(category.isExcludeFromBudget())
                .excludeFromTotals(category.isExcludeFromTotals())
                .build();
    }

    public static List<CategoryDto> fromEntities(List<Category> categories) {
        return categories.stream().map(CategoryDto::fromEntity).toList();
    }
}
