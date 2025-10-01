package com.mochafund.workspaceservice.merchant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mochafund.workspaceservice.common.annotations.PatchableField;
import com.mochafund.workspaceservice.common.entity.BaseEntity;
import com.mochafund.workspaceservice.common.patchable.Patchable;
import com.mochafund.workspaceservice.merchant.enums.MerchantStatus;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DynamicUpdate
@Entity
@Table(name = "merchants")
public class Merchant extends BaseEntity implements Patchable {

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @PatchableField
    @Column(name = "payee", nullable = false)
    private String payee;

    @PatchableField
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MerchantStatus status = MerchantStatus.ACTIVE;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;
}
