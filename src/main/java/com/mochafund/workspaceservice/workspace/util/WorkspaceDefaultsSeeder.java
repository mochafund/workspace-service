package com.mochafund.workspaceservice.workspace.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.workspaceservice.category.dto.CreateCategoryDto;
import com.mochafund.workspaceservice.category.entity.Category;
import com.mochafund.workspaceservice.category.repository.ICategoryRepository;
import com.mochafund.workspaceservice.category.service.CategoryService;
import com.mochafund.workspaceservice.tag.dto.CreateTagDto;
import com.mochafund.workspaceservice.tag.dto.UpdateTagDto;
import com.mochafund.workspaceservice.tag.entity.Tag;
import com.mochafund.workspaceservice.tag.enums.TagStatus;
import com.mochafund.workspaceservice.tag.repository.ITagRepository;
import com.mochafund.workspaceservice.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceDefaultsSeeder {

    private static final String CATEGORY_FIXTURE_PATH = "classpath:fixtures/defaults/workspace-default-categories.json";
    private static final String TAG_FIXTURE_PATH = "classpath:fixtures/defaults/workspace-default-tags.json";

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final ICategoryRepository categoryRepository;
    private final ITagRepository tagRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Transactional
    public void seedDefaults(UUID workspaceId) {
        Objects.requireNonNull(workspaceId, "workspaceId must not be null");

        if (!categoryRepository.existsByWorkspaceId(workspaceId)) {
            seedCategories(workspaceId, workspaceId);
        } else {
            log.debug("Skipping category seeding for workspace {} - categories already exist", workspaceId);
        }

        if (!tagRepository.existsByWorkspaceId(workspaceId)) {
            seedTags(workspaceId, workspaceId);
        } else {
            log.debug("Skipping tag seeding for workspace {} - tags already exist", workspaceId);
        }
    }

    private void seedCategories(UUID actorId, UUID workspaceId) {
        JsonNode root = readFixture(CATEGORY_FIXTURE_PATH);
        if (root == null || !root.isArray() || root.isEmpty()) {
            log.warn("Category fixture empty; nothing to seed for workspace {}", workspaceId);
            return;
        }

        for (JsonNode node : root) {
            persistCategoryTree(actorId, workspaceId, node, null);
        }

        log.info("Seeded {} default category groups for workspace {}", root.size(), workspaceId);
    }

    private void persistCategoryTree(UUID actorId, UUID workspaceId, JsonNode node, UUID parentId) {
        CreateCategoryDto dto = buildCategoryDto(node, parentId);
        Category category = categoryService.createCategory(actorId, workspaceId, dto);

        JsonNode children = node.get("children");
        if (children != null && children.isArray()) {
            for (JsonNode child : children) {
                persistCategoryTree(actorId, workspaceId, child, category.getId());
            }
        }
    }

    private void seedTags(UUID actorId, UUID workspaceId) {
        JsonNode root = readFixture(TAG_FIXTURE_PATH);
        if (root == null || !root.isArray() || root.isEmpty()) {
            log.warn("Tag fixture empty; nothing to seed for workspace {}", workspaceId);
            return;
        }

        List<Tag> tags = new ArrayList<>();
        for (JsonNode node : root) {
            CreateTagDto dto = buildTagDto(node);
            Tag tag = tagService.createTag(actorId, workspaceId, dto);

            TagStatus desiredStatus = parseStatus(node.path("status"));
            if (desiredStatus != null && desiredStatus != tag.getStatus()) {
                tagService.updateTag(workspaceId, tag.getId(), new UpdateTagDto(null, null, desiredStatus));
                tag.setStatus(desiredStatus);
            }

            tags.add(tag);
        }

        log.info("Seeded {} default tags for workspace {}", tags.size(), workspaceId);
    }

    private CreateCategoryDto buildCategoryDto(JsonNode node, UUID parentId) {
        CreateCategoryDto dto = new CreateCategoryDto();
        dto.setName(node.path("name").asText());
        dto.setDescription(readString(node, "description"));
        dto.setIsIncome(readBoolean(node, "income"));
        dto.setExcludeFromBudget(readBoolean(node, "excludeFromBudget"));
        dto.setExcludeFromTotals(readBoolean(node, "excludeFromTotals"));
        dto.setParentId(parentId);
        return dto;
    }

    private CreateTagDto buildTagDto(JsonNode node) {
        CreateTagDto dto = new CreateTagDto();
        dto.setName(node.path("name").asText());
        dto.setDescription(readString(node, "description"));
        return dto;
    }

    private Boolean readBoolean(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() ? node.get(fieldName).asBoolean() : null;
    }

    private String readString(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() ? node.get(fieldName).asText() : null;
    }

    private TagStatus parseStatus(JsonNode statusNode) {
        if (statusNode == null || statusNode.isNull() || !statusNode.isTextual()) {
            return null;
        }
        String raw = statusNode.asText();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return TagStatus.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            log.warn("Unknown tag status '{}' in fixture; defaulting to ACTIVE", raw);
            return TagStatus.ACTIVE;
        }
    }

    private JsonNode readFixture(String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            return objectMapper.readTree(resource.getInputStream());
        } catch (IOException ex) {
            log.error("Failed to read fixture {}", path, ex);
            return null;
        }
    }
}
