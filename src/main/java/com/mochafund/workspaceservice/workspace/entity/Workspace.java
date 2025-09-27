package com.mochafund.workspaceservice.workspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mochafund.workspaceservice.category.entity.Category;
import com.mochafund.workspaceservice.common.annotations.PatchableField;
import com.mochafund.workspaceservice.common.patchable.Patchable;
import com.mochafund.workspaceservice.tag.entity.Tag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DynamicUpdate
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "workspaces")
public class Workspace implements Patchable {

    @Id
    @Column(name = "id")
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PatchableField
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories;

}
