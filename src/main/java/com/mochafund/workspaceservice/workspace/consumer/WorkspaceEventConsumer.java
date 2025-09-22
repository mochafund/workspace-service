package com.mochafund.workspaceservice.workspace.consumer;

import com.mochafund.workspaceservice.common.util.CorrelationIdUtil;
import com.mochafund.workspaceservice.workspace.events.WorkspaceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkspaceEventConsumer {

    @KafkaListener(topics = "workspace.created", groupId = "workspace-service")
    public void handleWorkspaceCreated(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.created- Workspace: {}", event.getData().name());
        });
    }

    // TODO: workspace.deleted event
    @KafkaListener(topics = "workspace.deleted", groupId = "workspace-service")
    public void handleWorkspaceDeleted(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.deleted- Workspace: {}", event.getData().name());
        });
    }
}