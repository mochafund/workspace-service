package com.mochafund.workspaceservice.categories.service;

import com.mochafund.workspaceservice.categories.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.categories.dto.UpdateCategoryDto;
import com.mochafund.workspaceservice.categories.entity.Category;
import com.mochafund.workspaceservice.categories.enums.CategoryStatus;
import com.mochafund.workspaceservice.categories.repository.ICategoryRepository;
import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Category getCategory(UUID workspaceId, UUID categoryId) {
        return categoryRepository.findByWorkspaceIdAndId(workspaceId, categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional
    public Category createCategory(UUID userId, UUID workspaceId, CreateCategoryDto categoryDto) {
        Category category = categoryRepository.save(Category.builder()
                .workspaceId(workspaceId)
                .createdBy(userId)
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .isIncome(categoryDto.isIncome())
                .excludeFromBudget(categoryDto.isExcludeFromBudget())
                .excludeFromTotals(categoryDto.isExcludeFromTotals())
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

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(UUID workspaceId, UUID categoryId) {
        Category category = this.getCategory(workspaceId, categoryId);

        log.info("Deleting categoryId={}", category.getId());

        categoryRepository.deleteByWorkspaceIdAndId(category.getWorkspaceId(), category.getId());
    }
}
