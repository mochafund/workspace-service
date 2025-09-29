package com.mochafund.workspaceservice.workspace.service;


import com.mochafund.workspaceservice.workspace.dto.UpdateWorkspaceDto;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.events.WorkspaceEventPayload;

import java.util.UUID;

public interface IWorkspaceService {
    Workspace createWorkspace(WorkspaceEventPayload workspaceEvent);
    Workspace getWorkspace(UUID workspaceId);
    Workspace updateWorkspace(UUID workspaceId, UpdateWorkspaceDto workspaceDto);
    void deleteWorkspace(UUID workspaceId);
}
