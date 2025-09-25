package com.mochafund.workspaceservice.categories.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mochafund.workspaceservice.categories.enums.CategoryStatus;
import com.mochafund.workspaceservice.common.annotations.PatchableField;
import com.mochafund.workspaceservice.common.entity.BaseEntity;
import com.mochafund.workspaceservice.common.patchable.Patchable;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DynamicUpdate
@Entity
@Table(name = "categories")
public class Category extends BaseEntity implements Patchable {

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @PatchableField
    @Column(name = "name", nullable = false)
    private String name;

    @PatchableField
    @Column(name = "description")
    private String description;

    @PatchableField
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CategoryStatus status = CategoryStatus.ACTIVE;

    @PatchableField
    @Column(name = "is_income", nullable = false)
    private boolean isIncome = false;

    @PatchableField
    @Column(name = "exclude_from_budget", nullable = false)
    private boolean excludeFromBudget = false;

    @PatchableField
    @Column(name = "exclude_from_totals", nullable = false)
    private boolean excludeFromTotals = false;

    @PatchableField
    @Column(name = "parent_id")
    private UUID parentId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Category parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();
}
