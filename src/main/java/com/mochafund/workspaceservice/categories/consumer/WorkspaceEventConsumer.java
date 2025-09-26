package com.mochafund.workspaceservice.categories.consumer;

import com.mochafund.workspaceservice.categories.service.ICategoryService;
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

    private final ICategoryService categoryService;

    @KafkaListener(topics = "workspace.created", groupId = "workspace-service")
    public void handleWorkspaceCreated(WorkspaceEvent event) {
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            log.info("Processing workspace.created- Workspace: {}", event.getData().workspaceId());
        });
    }
}