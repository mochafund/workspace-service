package com.mochafund.workspaceservice.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mochafund.workspaceservice.account.enums.AccountSource;
import com.mochafund.workspaceservice.account.enums.AccountStatus;
import com.mochafund.workspaceservice.account.enums.AccountSubType;
import com.mochafund.workspaceservice.account.enums.CurrencyCode;
import com.mochafund.workspaceservice.account.enums.AccountType;
import com.mochafund.workspaceservice.common.annotations.PatchableField;
import com.mochafund.workspaceservice.common.entity.BaseEntity;
import com.mochafund.workspaceservice.common.patchable.Patchable;
import com.mochafund.workspaceservice.workspace.entity.Workspace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DynamicUpdate
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity implements Patchable {

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @PatchableField
    @Builder.Default
    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @PatchableField
    @Column(name = "name", nullable = false)
    private String name;

    @PatchableField
    @Column(name = "display_name")
    private String displayName;

    @PatchableField
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyCode currency = CurrencyCode.USD;

    @PatchableField
    @Column(name = "institution_name")
    private String institutionName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private AccountSource source = AccountSource.MANUAL;

    @PatchableField
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @PatchableField
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccountType type;

    @PatchableField
    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type", nullable = false)
    private AccountSubType subType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @PrePersist
    @PreUpdate
    private void validateTypeAndSubType() {
        if (type != null && subType != null) {
            type.validateSubType(subType);
        }
    }
}
