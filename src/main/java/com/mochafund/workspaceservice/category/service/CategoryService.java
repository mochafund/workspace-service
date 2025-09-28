package com.mochafund.workspaceservice.category.service;

import com.mochafund.workspaceservice.category.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.category.dto.UpdateCategoryDto;
import com.mochafund.workspaceservice.category.entity.Category;
import com.mochafund.workspaceservice.category.enums.CategoryStatus;
import com.mochafund.workspaceservice.category.repository.ICategoryRepository;
import com.mochafund.workspaceservice.common.exception.BadRequestException;
import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> listAllByWorkspaceId(UUID workspaceId) {
        return categoryRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .sorted(java.util.Comparator.comparing(Category::getName))
                .toList();
    }

    @Transactional(readOnly = true)
    public Category getCategory(UUID workspaceId, UUID categoryId) {
        return categoryRepository.findByWorkspaceIdAndId(workspaceId, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional
    public Category createCategory(UUID userId, UUID workspaceId, CreateCategoryDto categoryDto) {
        UUID parentId = categoryDto.getParentId();
        if (parentId != null) {
            validateParent(workspaceId, parentId);
        }

        Category category = CreateCategoryDto.fromDto(categoryDto);
        category.setWorkspaceId(workspaceId);
        category.setCreatedBy(userId);
        category.setParentId(parentId);

        category = categoryRepository.save(category);

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

    @Transactional
    public void deleteCategory(UUID workspaceId, UUID categoryId) {
        Category category = this.getCategory(workspaceId, categoryId);

        log.info("Deleting categoryId={}", category.getId());

        categoryRepository.deleteByWorkspaceIdAndId(category.getWorkspaceId(), category.getId());
    }

    private void validateParent(UUID workspaceId, UUID parentId) {
        Category parent = categoryRepository.findByWorkspaceIdAndId(workspaceId, parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));

        if (parent.getParentId() != null) {
            throw new BadRequestException("Parent category cannot have its own parent (maximum depth is 2)");
        }
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
}
