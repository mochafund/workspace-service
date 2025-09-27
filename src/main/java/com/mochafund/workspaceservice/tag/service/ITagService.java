package com.mochafund.workspaceservice.tag.service;

import com.mochafund.workspaceservice.tag.dto.CreateTagDto;
import com.mochafund.workspaceservice.tag.dto.UpdateTagDto;
import com.mochafund.workspaceservice.tag.entity.Tag;

import java.util.List;
import java.util.UUID;

public interface ITagService {
    Tag createTag(UUID userId, UUID workspaceId, CreateTagDto tagDto);
    Tag getTag(UUID workspaceId, UUID tagId);
    Tag updateTag(UUID workspaceId, UUID tagId, UpdateTagDto tagDto);
    void deleteTag(UUID workspaceId, UUID tagId);
    List<Tag> listAllByWorkspaceId(UUID workspaceId);
}
