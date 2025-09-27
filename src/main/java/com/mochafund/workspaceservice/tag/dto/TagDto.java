package com.mochafund.workspaceservice.tag.dto;

import com.mochafund.workspaceservice.common.dto.BaseDto;
import com.mochafund.workspaceservice.tag.entity.Tag;
import com.mochafund.workspaceservice.tag.enums.TagStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TagDto extends BaseDto {

    private UUID workspaceId;
    private UUID createdBy;
    private String name;
    private String description;
    private TagStatus status;

    public static TagDto fromEntity(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .workspaceId(tag.getWorkspaceId())
                .createdBy(tag.getCreatedBy())
                .name(tag.getName())
                .description(tag.getDescription())
                .status(tag.getStatus())
                .build();
    }

    public static List<TagDto> fromEntities(List<Tag> tags) {
        return tags.stream().map(TagDto::fromEntity).toList();
    }
}
