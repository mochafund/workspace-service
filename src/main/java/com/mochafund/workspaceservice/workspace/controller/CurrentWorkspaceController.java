package com.mochafund.workspaceservice.workspace.controller;

import com.mochafund.workspaceservice.common.annotations.WorkspaceId;
import com.mochafund.workspaceservice.workspace.dto.UpdateWorkspaceDto;
import com.mochafund.workspaceservice.workspace.dto.WorkspaceDto;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.service.IWorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/workspaces/current")
public class CurrentWorkspaceController {

    private final IWorkspaceService workspaceService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkspaceDto> getCurrentWorkspace(@WorkspaceId UUID workspaceId) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        return ResponseEntity.ok().body(WorkspaceDto.fromEntity(workspace));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkspaceDto> updateCurrentWorkspace(
            @WorkspaceId UUID workspaceId,
            @Valid @RequestBody UpdateWorkspaceDto updateDto
    ) {
        Workspace updatedWorkspace = workspaceService.updateWorkspace(workspaceId, updateDto);
        return ResponseEntity.ok().body(WorkspaceDto.fromEntity(updatedWorkspace));
    }
}
