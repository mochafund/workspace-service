package com.mochafund.workspaceservice.workspace.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.workspaceservice.category.entity.Category;
import com.mochafund.workspaceservice.category.enums.CategoryStatus;
import com.mochafund.workspaceservice.category.repository.ICategoryRepository;
import com.mochafund.workspaceservice.tag.entity.Tag;
import com.mochafund.workspaceservice.tag.enums.TagStatus;
import com.mochafund.workspaceservice.tag.repository.ITagRepository;
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

    @Transactional
    public void seedDefaults(UUID workspaceId) {
        Objects.requireNonNull(workspaceId, "workspaceId must not be null");

        if (!categoryRepository.existsByWorkspaceId(workspaceId)) {
            seedCategories(workspaceId);
        } else {
            log.debug("Skipping category seeding for workspace {} - categories already exist", workspaceId);
        }

        if (!tagRepository.existsByWorkspaceId(workspaceId)) {
            seedTags(workspaceId);
        } else {
            log.debug("Skipping tag seeding for workspace {} - tags already exist", workspaceId);
        }
    }

    private void seedCategories(UUID workspaceId) {
        JsonNode root = readFixture(CATEGORY_FIXTURE_PATH);
        if (root == null || !root.isArray() || root.isEmpty()) {
            log.warn("Category fixture empty; nothing to seed for workspace {}", workspaceId);
            return;
        }

        for (JsonNode node : root) {
            persistCategoryTree(workspaceId, node, null);
        }

        log.info("Seeded {} default category groups for workspace {}", root.size(), workspaceId);
    }

    private void persistCategoryTree(UUID workspaceId, JsonNode node, UUID parentId) {
        Category category = categoryRepository.save(buildCategoryEntity(workspaceId, parentId, node));

        JsonNode children = node.get("children");
        if (children != null && children.isArray()) {
            for (JsonNode child : children) {
                persistCategoryTree(workspaceId, child, category.getId());
            }
        }
    }

    private void seedTags(UUID workspaceId) {
        JsonNode root = readFixture(TAG_FIXTURE_PATH);
        if (root == null || !root.isArray() || root.isEmpty()) {
            log.warn("Tag fixture empty; nothing to seed for workspace {}", workspaceId);
            return;
        }

        List<Tag> tags = new ArrayList<>();
        for (JsonNode node : root) {
            tags.add(buildTagEntity(workspaceId, node));
        }

        tagRepository.saveAll(tags);
        log.info("Seeded {} default tags for workspace {}", tags.size(), workspaceId);
    }

    private Category buildCategoryEntity(UUID workspaceId, UUID parentId, JsonNode node) {
        boolean income = node.path("income").asBoolean(false);
        boolean excludeFromBudget = node.path("excludeFromBudget").asBoolean(false);
        boolean excludeFromTotals = node.path("excludeFromTotals").asBoolean(false);

        return Category.builder()
                .workspaceId(workspaceId)
                .createdBy(workspaceId)
                .parentId(parentId)
                .name(node.path("name").asText())
                .description(node.path("description").isNull() ? null : node.get("description").asText())
                .isIncome(income)
                .excludeFromBudget(excludeFromBudget)
                .excludeFromTotals(excludeFromTotals)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    private Tag buildTagEntity(UUID workspaceId, JsonNode node) {
        TagStatus status = TagStatus.ACTIVE;
        if (node.hasNonNull("status")) {
            try {
                status = TagStatus.valueOf(node.get("status").asText().toUpperCase());
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown tag status '{}' in fixture; defaulting to ACTIVE", node.get("status").asText());
            }
        }

        return Tag.builder()
                .workspaceId(workspaceId)
                .createdBy(workspaceId)
                .name(node.path("name").asText())
                .description(node.path("description").isNull() ? null : node.get("description").asText())
                .status(status)
                .build();
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
