package com.mochafund.workspaceservice.workspace.service;

import com.mochafund.workspaceservice.common.events.EventEnvelope;
import com.mochafund.workspaceservice.common.events.EventType;
import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import com.mochafund.workspaceservice.kafka.KafkaProducer;
import com.mochafund.workspaceservice.workspace.dto.UpdateWorkspaceDto;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.events.WorkspaceEventPayload;
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
    private final KafkaProducer kafkaProducer;

    @Transactional(readOnly = true)
    public Workspace getWorkspace(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(
                () -> new ResourceNotFoundException("Workspace not found"));
    }

    @Transactional
    public Workspace createWorkspace(WorkspaceEventPayload workspaceEvent) {
        Workspace workspace = workspaceRepository.save(Workspace.builder()
                .id(workspaceEvent.getWorkspaceId())
                .name(workspaceEvent.getName())
                .build()
        );

        kafkaProducer.send(EventEnvelope.<WorkspaceEventPayload>builder()
                .type(EventType.WORKSPACE_CREATED)
                .payload(WorkspaceEventPayload.builder()
                        .workspaceId(workspace.getId())
                        .name(workspace.getName())
                        .build())
                .build());

        return workspace;
    }

    @Transactional
    public Workspace updateWorkspace(UUID workspaceId, UpdateWorkspaceDto workspaceDto) {
        log.info("Updating workspace with ID: {}", workspaceId);

        Workspace workspace = this.getWorkspace(workspaceId);
        workspace.patchFrom(workspaceDto);
        Workspace updatedWorkspace = workspaceRepository.save(workspace);

        kafkaProducer.send(EventEnvelope.<WorkspaceEventPayload>builder()
                .type(EventType.WORKSPACE_UPDATED)
                .payload(WorkspaceEventPayload.builder()
                        .workspaceId(updatedWorkspace.getId())
                        .name(updatedWorkspace.getName())
                        .build())
                .build());

        return updatedWorkspace;
    }

    @Transactional
    public void deleteWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        workspaceRepository.deleteById(workspace.getId());

        kafkaProducer.send(EventEnvelope.<WorkspaceEventPayload>builder()
                .type(EventType.WORKSPACE_DELETED)
                .payload(WorkspaceEventPayload.builder()
                        .workspaceId(workspace.getId())
                        .name(workspace.getName())
                        .build())
                .build());
    }
}
