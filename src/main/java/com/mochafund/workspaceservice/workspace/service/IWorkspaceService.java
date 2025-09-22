package com.mochafund.workspaceservice.workspace.service;


import com.mochafund.workspaceservice.workspace.entity.Workspace;

import java.util.UUID;

public interface IWorkspaceService {
    Workspace createWorkspace(UUID workspaceId);
}
