package com.mochafund.workspaceservice.categories.seed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CategorySeed(
        String name,
        String description,
        @JsonProperty("income") Boolean income,
        @JsonProperty("excludeFromBudget") Boolean excludeFromBudget,
        @JsonProperty("excludeFromTotals") Boolean excludeFromTotals,
        @JsonProperty("children") List<CategorySeed> children
) {
    @Override
    public List<CategorySeed> children() {
        return children == null ? List.of() : children;
    }
}
