package com.mochafund.workspaceservice.workspace.repository;

import com.mochafund.workspaceservice.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IWorkspaceRepository extends JpaRepository<Workspace, UUID> {
    void deleteById(UUID workspaceId);
}
