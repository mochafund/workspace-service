package com.mochafund.workspaceservice.workspace.consumer;

import com.mochafund.workspaceservice.common.util.CorrelationIdUtil;
import com.mochafund.workspaceservice.kafka.KafkaProducer;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import com.mochafund.workspaceservice.workspace.events.WorkspaceEvent;
import com.mochafund.workspaceservice.workspace.service.IWorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkspaceEventConsumer {

    private final IWorkspaceService workspaceService;
    private final KafkaProducer kafkaProducer;

    @Transactional
    @KafkaListener(topics = "workspace.provisioning", groupId = "workspace-service")
    public void handleWorkspaceProvisioning(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.provisioning- Workspace: {}", event.getData().workspaceId());

            Workspace workspace = workspaceService.createWorkspace(event.getData().workspaceId());
            log.info("Successfully created workspace {}", workspace.getId());

            kafkaProducer.send(WorkspaceEvent.builder()
                    .type("workspace.created")
                    .correlationId(event.getCorrelationId())
                    .data(WorkspaceEvent.Data.builder()
                            .workspaceId(workspace.getId())
                            .build())
                    .build());
        });
    }

    // TODO: workspace.deleted event
    @KafkaListener(topics = "workspace.deleted", groupId = "workspace-service")
    public void handleWorkspaceDeleted(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.deleted- Workspace: {}", event.getData().workspaceId());
        });
    }
}