package com.mochafund.workspaceservice.workspace.dto;

import com.mochafund.workspaceservice.common.dto.BaseDto;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkspaceDto extends BaseDto {

    private String name;

    public static WorkspaceDto fromEntity(Workspace workspace) {
        return WorkspaceDto.builder()
                .id(workspace.getId())
                .createdAt(workspace.getCreatedAt())
                .updatedAt(workspace.getUpdatedAt())
                .name(workspace.getName())
                .build();
    }

    public static List<WorkspaceDto> fromEntities(List<Workspace> workspaces) {
        return workspaces.stream().map(WorkspaceDto::fromEntity).toList();
    }
}
