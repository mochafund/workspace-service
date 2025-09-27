package com.mochafund.workspaceservice.tag.service;

import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import com.mochafund.workspaceservice.tag.dto.CreateTagDto;
import com.mochafund.workspaceservice.tag.dto.UpdateTagDto;
import com.mochafund.workspaceservice.tag.entity.Tag;
import com.mochafund.workspaceservice.tag.enums.TagStatus;
import com.mochafund.workspaceservice.tag.repository.ITagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class TagService implements ITagService {

    private final ITagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<Tag> listAllByWorkspaceId(UUID workspaceId) {
        return tagRepository.findAllByWorkspaceId(workspaceId);
    }

    @Transactional(readOnly = true)
    public Tag getTag(UUID workspaceId, UUID tagId) {
        return tagRepository.findByWorkspaceIdAndId(workspaceId, tagId).orElseThrow(
                () -> new ResourceNotFoundException("Tag not found"));
    }

    @Transactional
    public Tag createTag(UUID userId, UUID workspaceId, CreateTagDto tagDto) {
        Tag tag = tagRepository.save(Tag.builder()
                .workspaceId(workspaceId)
                .createdBy(userId)
                .name(tagDto.getName())
                .status(TagStatus.ACTIVE)
                .build());

        log.info("Created tagId={} for name={}", tag.getId(), tagDto.getName());
        return tag;
    }

    @Transactional
    public Tag updateTag(UUID workspaceId, UUID tagId, UpdateTagDto tagDto) {
        log.info("Updating tagId={}", tagId);

        Tag tag = this.getTag(workspaceId, tagId);
        tag.patchFrom(tagDto);

        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(UUID workspaceId, UUID tagId) {
       Tag tag = this.getTag(workspaceId, tagId);

       log.info("Deleting tagId={}", tag.getId());

       tagRepository.deleteByWorkspaceIdAndId(tag.getWorkspaceId(), tag.getId());
    }
}
