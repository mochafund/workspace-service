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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CategoryTreeDto extends BaseDto {

    private UUID workspaceId;
    private UUID createdBy;
    private UUID parentId;
    private String name;
    private String description;
    private CategoryStatus status;
    private boolean isIncome;
    private boolean excludeFromBudget;
    private boolean excludeFromTotals;
    private boolean isGroup;
    private List<CategoryTreeDto> children;

    public static CategoryTreeDto fromEntity(Category category, List<CategoryTreeDto> children) {
        List<CategoryTreeDto> safeChildren = children == null ? List.of() : List.copyOf(children);
        boolean isGroup = !safeChildren.isEmpty();

        return CategoryTreeDto.builder()
                .id(category.getId())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .workspaceId(category.getWorkspaceId())
                .createdBy(category.getCreatedBy())
                .parentId(category.getParentId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .isIncome(category.isIncome())
                .excludeFromBudget(category.isExcludeFromBudget())
                .excludeFromTotals(category.isExcludeFromTotals())
                .isGroup(isGroup)
                .children(safeChildren)
                .build();
    }

    public static List<CategoryTreeDto> fromEntities(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }

        Map<UUID, List<Category>> byParent = new HashMap<>();
        for (Category category : categories) {
            UUID parentId = category.getParentId();
            byParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(category);
        }

        return buildTree(null, byParent);
    }

    private static List<CategoryTreeDto> buildTree(UUID parentId, Map<UUID, List<Category>> byParent) {
        return byParent.getOrDefault(parentId, List.of()).stream()
                .sorted(java.util.Comparator.comparing(Category::getCreatedAt))
                .map(category -> {
                    List<CategoryTreeDto> children = buildTree(category.getId(), byParent);
                    return CategoryTreeDto.fromEntity(category, children);
                })
                .toList();
    }
}
