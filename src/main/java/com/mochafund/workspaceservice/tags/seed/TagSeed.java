package com.mochafund.workspaceservice.tags.seed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TagSeed(
        String name,
        String description,
        String status
) {}
