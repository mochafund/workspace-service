package com.mochafund.workspaceservice.tag.controller;

import com.mochafund.workspaceservice.common.annotations.UserId;
import com.mochafund.workspaceservice.common.annotations.WorkspaceId;
import com.mochafund.workspaceservice.tag.dto.CreateTagDto;
import com.mochafund.workspaceservice.tag.dto.TagDto;
import com.mochafund.workspaceservice.tag.dto.UpdateTagDto;
import com.mochafund.workspaceservice.tag.entity.Tag;
import com.mochafund.workspaceservice.tag.service.ITagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

    private final ITagService tagService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDto>> getAllTags(@WorkspaceId UUID workspaceId) {
        List<Tag> tags = tagService.listAllByWorkspaceId(workspaceId);
        return ResponseEntity.ok().body(TagDto.fromEntities(tags));
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(value = "/{tagId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> getTag(@WorkspaceId UUID workspaceId, @PathVariable UUID tagId) {
        Tag tag = tagService.getTag(workspaceId, tagId);
        return ResponseEntity.ok().body(TagDto.fromEntity(tag));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> createTag(
            @UserId UUID userId, @WorkspaceId UUID workspaceId,
            @Valid @RequestBody CreateTagDto tagDto
    ) {
        Tag tag = tagService.createTag(userId, workspaceId, tagDto);
        return ResponseEntity.status(201).body(TagDto.fromEntity(tag));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PatchMapping(value = "/{tagId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> updateTag(
            @WorkspaceId UUID workspaceId, @PathVariable UUID tagId,
            @Valid @RequestBody UpdateTagDto tagDto) {
        Tag tag = tagService.updateTag(workspaceId, tagId, tagDto);
        return ResponseEntity.ok().body(TagDto.fromEntity(tag));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @DeleteMapping(value = "/{tagId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTag(@WorkspaceId UUID workspaceId, @PathVariable UUID tagId) {
        tagService.deleteTag(workspaceId, tagId);
        return ResponseEntity.noContent().build();
    }
}
