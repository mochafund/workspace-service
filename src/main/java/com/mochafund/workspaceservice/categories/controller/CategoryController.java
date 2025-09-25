package com.mochafund.workspaceservice.categories.controller;

import com.mochafund.workspaceservice.categories.dto.CategoryDto;
import com.mochafund.workspaceservice.categories.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.categories.dto.UpdateCategoryDto;
import com.mochafund.workspaceservice.categories.entity.Category;
import com.mochafund.workspaceservice.categories.service.ICategoryService;
import com.mochafund.workspaceservice.common.annotations.UserId;
import com.mochafund.workspaceservice.common.annotations.WorkspaceId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getAllCategories(@WorkspaceId UUID workspaceId) {
        List<Category> categories = categoryService.listAllByWorkspaceId(workspaceId);
        return ResponseEntity.ok().body(CategoryDto.fromEntities(categories));
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategory(@WorkspaceId UUID workspaceId, @PathVariable UUID categoryId) {
        Category category = categoryService.getCategory(workspaceId, categoryId);
        return ResponseEntity.ok().body(CategoryDto.fromEntity(category));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> createCategory(
            @UserId UUID userId, @WorkspaceId UUID workspaceId,
            @Valid @RequestBody CreateCategoryDto categoryDto
    ) {
        Category category = categoryService.createCategory(userId, workspaceId, categoryDto);
        return ResponseEntity.status(201).body(CategoryDto.fromEntity(category));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PatchMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(
            @WorkspaceId UUID workspaceId, @PathVariable UUID categoryId,
            @Valid @RequestBody UpdateCategoryDto categoryDto) {
        Category category = categoryService.updateCategory(workspaceId, categoryId, categoryDto);
        return ResponseEntity.ok().body(CategoryDto.fromEntity(category));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCategory(@WorkspaceId UUID workspaceId, @PathVariable UUID categoryId) {
        categoryService.deleteCategory(workspaceId, categoryId);
        return ResponseEntity.noContent().build();
    }
}
