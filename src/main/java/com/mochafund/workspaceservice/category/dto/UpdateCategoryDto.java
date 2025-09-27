package com.mochafund.workspaceservice.category.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mochafund.workspaceservice.category.enums.CategoryStatus;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {

    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String name;

    private String description;
    private CategoryStatus status;
    private Boolean isIncome;
    private Boolean excludeFromBudget;
    private Boolean excludeFromTotals;

    @Setter(AccessLevel.NONE)
    private UUID parentId;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean parentIdSpecified;

    @JsonSetter("parentId")
    public void assignParentId(UUID parentId) {
        this.parentId = parentId;
        this.parentIdSpecified = true;
    }
}
