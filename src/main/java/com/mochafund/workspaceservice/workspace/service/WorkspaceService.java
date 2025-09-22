package com.mochafund.workspaceservice.workspace.service;

import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.repository.IWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkspaceService implements IWorkspaceService {

    private final IWorkspaceRepository workspaceRepository;

    @Transactional
    public Workspace createWorkspace(UUID workspaceId) {
        return workspaceRepository.save(Workspace.builder()
                .id(workspaceId)
                .build()
        );
    }
}
