package com.mochafund.workspaceservice.categories.service;

import com.mochafund.workspaceservice.categories.dto.CategoryDto;
import com.mochafund.workspaceservice.categories.dto.CategoryTreeDto;
import com.mochafund.workspaceservice.categories.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.categories.dto.UpdateCategoryDto;
import com.mochafund.workspaceservice.categories.entity.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    Category createCategory(UUID userId, UUID workspaceId, CreateCategoryDto categoryDto);
    Category getCategory(UUID workspaceId, UUID categoryId);
    CategoryDto getCategoryDto(UUID workspaceId, UUID categoryId);
    Category updateCategory(UUID workspaceId, UUID categoryId, UpdateCategoryDto categoryDto);
    void deleteCategory(UUID workspaceId, UUID categoryId);
    List<Category> listAllByWorkspaceId(UUID workspaceId);
    List<CategoryDto> listAllDtosByWorkspaceId(UUID workspaceId);
    List<CategoryTreeDto> listCategoryTreeByWorkspaceId(UUID workspaceId);
}
