package com.mochafund.workspaceservice.tags.service;

import com.mochafund.workspaceservice.tags.dto.CreateTagDto;
import com.mochafund.workspaceservice.tags.dto.UpdateTagDto;
import com.mochafund.workspaceservice.tags.entity.Tag;

import java.util.List;
import java.util.UUID;

public interface ITagService {
    Tag createTag(UUID userId, UUID workspaceId, CreateTagDto tagDto);
    Tag getTag(UUID workspaceId, UUID tagId);
    Tag updateTag(UUID workspaceId, UUID tagId, UpdateTagDto tagDto);
    void deleteTag(UUID workspaceId, UUID tagId);
    List<Tag> listAllByWorkspaceId(UUID workspaceId);
}
