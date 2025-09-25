package com.mochafund.workspaceservice.categories.repository;

import com.mochafund.workspaceservice.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByWorkspaceIdAndId(UUID workspaceId, UUID categoryId);
    List<Category> findAllByWorkspaceId(UUID workspaceId);
    void deleteByWorkspaceIdAndId(UUID workspaceId, UUID categoryId);
}
