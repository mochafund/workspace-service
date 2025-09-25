package com.mochafund.workspaceservice.workspace.consumer;

import com.mochafund.workspaceservice.common.util.CorrelationIdUtil;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.events.WorkspaceEvent;
import com.mochafund.workspaceservice.workspace.service.IWorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkspaceEventConsumer {

    private final IWorkspaceService workspaceService;

    @KafkaListener(topics = "workspace.provisioning", groupId = "workspace-service")
    public void handleWorkspaceProvisioning(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.provisioning- Workspace: {}", event.getData().workspaceId());

            Workspace workspace = workspaceService.createWorkspace(event.getData());
            log.info("Successfully created workspace {}", workspace.getId());
        });
    }

    @KafkaListener(topics = "workspace.deleted.initialized", groupId = "workspace-service")
    public void handleWorkspaceDeleted(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.deleted.initialized- Workspace: {}", event.getData().workspaceId());

            workspaceService.deleteWorkspace(event.getData().workspaceId());
            log.info("Successfully deleted workspace {}", event.getData().workspaceId());
        });
    }
}