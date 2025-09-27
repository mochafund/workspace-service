package com.mochafund.workspaceservice.category.service;

import com.mochafund.workspaceservice.category.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.category.dto.UpdateCategoryDto;
import com.mochafund.workspaceservice.category.entity.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    Category createCategory(UUID userId, UUID workspaceId, CreateCategoryDto categoryDto);
    Category getCategory(UUID workspaceId, UUID categoryId);
    Category updateCategory(UUID workspaceId, UUID categoryId, UpdateCategoryDto categoryDto);
    void deleteCategory(UUID workspaceId, UUID categoryId);
    List<Category> listAllByWorkspaceId(UUID workspaceId);
}
