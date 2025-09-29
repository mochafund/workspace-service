package com.mochafund.workspaceservice.workspace.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.workspaceservice.common.events.EventEnvelope;
import com.mochafund.workspaceservice.common.events.EventType;
import com.mochafund.workspaceservice.common.util.CorrelationIdUtil;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.events.WorkspaceEventPayload;
import com.mochafund.workspaceservice.workspace.service.IWorkspaceService;
import com.mochafund.workspaceservice.workspace.util.WorkspaceDefaultsSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkspaceEventConsumer {

    private final IWorkspaceService workspaceService;
    private final WorkspaceDefaultsSeeder workspaceDefaultsSeeder;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = EventType.WORKSPACE_PROVISIONING, groupId = "workspace-service")
    public void handleWorkspaceProvisioning(String message) {
        EventEnvelope<WorkspaceEventPayload> event = readEnvelope(message, WorkspaceEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            WorkspaceEventPayload payload = event.getPayload();
            log.info("Processing workspace.provisioning- Workspace: {}", payload.getWorkspaceId());

            Workspace workspace = workspaceService.createWorkspace(payload);
            log.info("Successfully created workspace {}", workspace.getId());
        });
    }

    @KafkaListener(topics = EventType.WORKSPACE_CREATED, groupId = "workspace-service")
    public void handleWorkspaceCreated(String message) {
        EventEnvelope<WorkspaceEventPayload> event = readEnvelope(message, WorkspaceEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            WorkspaceEventPayload payload = event.getPayload();
            log.info("Processing workspace.created- Workspace: {}", payload.getWorkspaceId());
            try {
                workspaceDefaultsSeeder.seedDefaults(payload.getWorkspaceId());
            } catch (Exception ex) {
                log.error("Failed to seed defaults for workspace {}", payload.getWorkspaceId(), ex);
            }
        });
    }

    @KafkaListener(topics = EventType.WORKSPACE_DELETED_INITIALIZED, groupId = "workspace-service")
    public void handleWorkspaceDeleted(String message) {
        EventEnvelope<WorkspaceEventPayload> event = readEnvelope(message, WorkspaceEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            WorkspaceEventPayload payload = event.getPayload();
            log.info("Processing workspace.deleted.initialized- Workspace: {}", payload.getWorkspaceId());

            workspaceService.deleteWorkspace(payload.getWorkspaceId());
            log.info("Successfully deleted workspace {}", payload.getWorkspaceId());
        });
    }

    private <T> EventEnvelope<T> readEnvelope(String message, Class<T> payloadType) {
        try {
            return objectMapper.readValue(
                    message,
                    objectMapper.getTypeFactory().constructParametricType(EventEnvelope.class, payloadType)
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse event envelope", e);
        }
    }
}