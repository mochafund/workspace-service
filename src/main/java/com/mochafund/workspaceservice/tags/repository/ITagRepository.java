package com.mochafund.workspaceservice.tags.repository;

import com.mochafund.workspaceservice.tags.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByWorkspaceIdAndId(UUID workspaceId, UUID tagId);
    List<Tag> findAllByWorkspaceId(UUID workspaceId);
    void deleteByWorkspaceIdAndId(UUID workspaceId, UUID tagId);
    boolean existsByWorkspaceId(UUID workspaceId);
}
