package com.mochafund.workspaceservice.categories.service;

import com.mochafund.workspaceservice.categories.dto.CategoryDto;
import com.mochafund.workspaceservice.categories.dto.CategoryTreeDto;
import com.mochafund.workspaceservice.categories.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.categories.dto.UpdateCategoryDto;
import com.mochafund.workspaceservice.categories.entity.Category;
import com.mochafund.workspaceservice.categories.enums.CategoryStatus;
import com.mochafund.workspaceservice.categories.repository.ICategoryRepository;
import com.mochafund.workspaceservice.common.exception.BadRequestException;
import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> listAllByWorkspaceId(UUID workspaceId) {
        return categoryRepository.findAllByWorkspaceId(workspaceId);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> listAllDtosByWorkspaceId(UUID workspaceId) {
        return categoryRepository.findAllByWorkspaceId(workspaceId).stream()
                .sorted(Comparator.comparing(Category::getCreatedAt))
                .map(CategoryDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryTreeDto> listCategoryTreeByWorkspaceId(UUID workspaceId) {
        List<Category> categories = categoryRepository.findAllByWorkspaceId(workspaceId);
        if (categories.isEmpty()) {
            return List.of();
        }

        Map<UUID, List<Category>> byParent = new HashMap<>();
        for (Category category : categories) {
            UUID parentId = category.getParentId();
            byParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(category);
        }

        return buildTree(null, byParent);
    }

    private List<CategoryTreeDto> buildTree(UUID parentId, Map<UUID, List<Category>> byParent) {
        return byParent.getOrDefault(parentId, List.of()).stream()
                .sorted(Comparator.comparing(Category::getCreatedAt))
                .map(category -> {
                    List<CategoryTreeDto> children = buildTree(category.getId(), byParent);
                    return CategoryTreeDto.fromEntity(category, children);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Category getCategory(UUID workspaceId, UUID categoryId) {
        return categoryRepository.findByWorkspaceIdAndId(workspaceId, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryDto(UUID workspaceId, UUID categoryId) {
        Category category = getCategory(workspaceId, categoryId);
        return CategoryDto.fromEntity(category);
    }

    @Transactional
    public Category createCategory(UUID userId, UUID workspaceId, CreateCategoryDto categoryDto) {
        UUID parentId = categoryDto.getParentId();
        if (parentId != null) {
            validateParent(workspaceId, parentId);
        }

        boolean income = Boolean.TRUE.equals(categoryDto.getIncome());
        boolean excludeFromBudget = Boolean.TRUE.equals(categoryDto.getExcludeFromBudget());
        boolean excludeFromTotals = Boolean.TRUE.equals(categoryDto.getExcludeFromTotals());

        Category category = categoryRepository.save(Category.builder()
                .workspaceId(workspaceId)
                .createdBy(userId)
                .parentId(parentId)
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .isIncome(income)
                .excludeFromBudget(excludeFromBudget)
                .excludeFromTotals(excludeFromTotals)
                .status(CategoryStatus.ACTIVE)
                .build());

        log.info("Created categoryId={} for name={}", category.getId(), categoryDto.getName());
        return category;
    }

    @Transactional
    public Category updateCategory(UUID workspaceId, UUID categoryId, UpdateCategoryDto categoryDto) {
        log.info("Updating categoryId={}", categoryId);

        Category category = this.getCategory(workspaceId, categoryId);
        category.patchFrom(categoryDto);

        if (categoryDto.isParentIdSpecified()) {
            UUID parentId = categoryDto.getParentId();
            if (parentId != null) {
                if (parentId.equals(categoryId)) {
                    throw new BadRequestException("Category cannot be its own parent");
                }
                validateParent(workspaceId, parentId);
                ensureNoCircularReference(categoryId, parentId);
            }
            category.setParentId(parentId);
        }

        return categoryRepository.save(category);
    }

    private Category validateParent(UUID workspaceId, UUID parentId) {
        Category parent = categoryRepository.findByWorkspaceIdAndId(workspaceId, parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));

        if (parent.getParentId() != null) {
            throw new BadRequestException("Parent category cannot have its own parent (maximum depth is 2)");
        }

        return parent;
    }

    private void ensureNoCircularReference(UUID categoryId, UUID parentId) {
        Set<UUID> visited = new HashSet<>();
        UUID current = parentId;

        while (current != null) {
            if (!visited.add(current) || categoryId.equals(current)) {
                throw new BadRequestException("Circular category relationship detected");
            }

            current = categoryRepository.findById(current)
                    .map(Category::getParentId)
                    .orElse(null);
        }
    }

    @Transactional
    public void deleteCategory(UUID workspaceId, UUID categoryId) {
        Category category = this.getCategory(workspaceId, categoryId);

        log.info("Deleting categoryId={}", category.getId());

        categoryRepository.deleteByWorkspaceIdAndId(category.getWorkspaceId(), category.getId());
    }
}
