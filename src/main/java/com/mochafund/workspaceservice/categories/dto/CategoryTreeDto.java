package com.mochafund.workspaceservice.categories.dto;

import com.mochafund.workspaceservice.categories.entity.Category;
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
public class CategoryTreeDto extends CategoryDto {

    private boolean isGroup;
    private List<CategoryTreeDto> children;

    public static CategoryTreeDto fromEntity(Category category, List<CategoryTreeDto> children) {
        CategoryDto base = CategoryDto.fromEntity(category);
        List<CategoryTreeDto> safeChildren = children == null ? List.of() : List.copyOf(children);
        boolean group = !safeChildren.isEmpty();

        return CategoryTreeDto.builder()
                .id(base.getId())
                .createdAt(base.getCreatedAt())
                .updatedAt(base.getUpdatedAt())
                .workspaceId(base.getWorkspaceId())
                .createdBy(base.getCreatedBy())
                .parentId(base.getParentId())
                .name(base.getName())
                .description(base.getDescription())
                .status(base.getStatus())
                .isIncome(base.isIncome())
                .excludeFromBudget(base.isExcludeFromBudget())
                .excludeFromTotals(base.isExcludeFromTotals())
                .isGroup(group)
                .children(safeChildren)
                .build();
    }

    public static List<CategoryTreeDto> buildTree(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }

        Map<UUID, List<Category>> byParent = new HashMap<>();
        for (Category category : categories) {
            UUID parentId = category.getParentId();
            byParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(category);
        }

        return buildBranch(null, byParent);
    }

    private static List<CategoryTreeDto> buildBranch(UUID parentId, Map<UUID, List<Category>> byParent) {
        return byParent.getOrDefault(parentId, List.of()).stream()
                .sorted(java.util.Comparator.comparing(Category::getCreatedAt))
                .map(category -> {
                    List<CategoryTreeDto> children = buildBranch(category.getId(), byParent);
                    return fromEntity(category, children);
                })
                .toList();
    }
}
