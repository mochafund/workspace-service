package com.mochafund.workspaceservice.workspace.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.workspaceservice.categories.entity.Category;
import com.mochafund.workspaceservice.categories.enums.CategoryStatus;
import com.mochafund.workspaceservice.categories.repository.ICategoryRepository;
import com.mochafund.workspaceservice.categories.seed.CategorySeed;
import com.mochafund.workspaceservice.tags.entity.Tag;
import com.mochafund.workspaceservice.tags.enums.TagStatus;
import com.mochafund.workspaceservice.tags.repository.ITagRepository;
import com.mochafund.workspaceservice.tags.seed.TagSeed;
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
        List<CategorySeed> seeds = readCategoriesFixture();
        if (seeds.isEmpty()) {
            log.warn("Category fixture empty; nothing to seed for workspace {}", workspaceId);
            return;
        }

        for (CategorySeed seed : seeds) {
            Category parent = categoryRepository.save(buildCategoryEntity(seed, workspaceId, null));
            if (!seed.children().isEmpty()) {
                List<Category> children = new ArrayList<>();
                for (CategorySeed childSeed : seed.children()) {
                    children.add(buildCategoryEntity(childSeed, workspaceId, parent.getId()));
                }
                categoryRepository.saveAll(children);
            }
        }

        log.info("Seeded {} default category groups for workspace {}", seeds.size(), workspaceId);
    }

    private void seedTags(UUID workspaceId) {
        List<TagSeed> seeds = readTagFixture();
        if (seeds.isEmpty()) {
            log.warn("Tag fixture empty; nothing to seed for workspace {}", workspaceId);
            return;
        }

        List<Tag> tags = seeds.stream()
                .map(seed -> buildTagEntity(seed, workspaceId))
                .toList();
        tagRepository.saveAll(tags);

        log.info("Seeded {} default tags for workspace {}", seeds.size(), workspaceId);
    }

    private Category buildCategoryEntity(CategorySeed seed, UUID workspaceId, UUID parentId) {
        boolean income = Boolean.TRUE.equals(seed.income());
        boolean excludeFromBudget = Boolean.TRUE.equals(seed.excludeFromBudget());
        boolean excludeFromTotals = Boolean.TRUE.equals(seed.excludeFromTotals());

        return Category.builder()
                .workspaceId(workspaceId)
                .createdBy(workspaceId)
                .parentId(parentId)
                .name(seed.name())
                .description(seed.description())
                .isIncome(income)
                .excludeFromBudget(excludeFromBudget)
                .excludeFromTotals(excludeFromTotals)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    private Tag buildTagEntity(TagSeed seed, UUID workspaceId) {
        TagStatus status = TagStatus.ACTIVE;
        if (seed.status() != null) {
            try {
                status = TagStatus.valueOf(seed.status().toUpperCase());
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown tag status '{}' in fixture; defaulting to ACTIVE", seed.status());
            }
        }

        return Tag.builder()
                .workspaceId(workspaceId)
                .createdBy(workspaceId)
                .name(seed.name())
                .description(seed.description())
                .status(status)
                .build();
    }

    private List<CategorySeed> readCategoriesFixture() {
        try {
            Resource resource = resourceLoader.getResource(CATEGORY_FIXTURE_PATH);
            return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
        } catch (IOException ex) {
            log.error("Failed to read category fixture", ex);
            return List.of();
        }
    }

    private List<TagSeed> readTagFixture() {
        try {
            Resource resource = resourceLoader.getResource(TAG_FIXTURE_PATH);
            return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
        } catch (IOException ex) {
            log.error("Failed to read tag fixture", ex);
            return List.of();
        }
    }
}
