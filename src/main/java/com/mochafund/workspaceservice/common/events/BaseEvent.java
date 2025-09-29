package com.mochafund.workspaceservice.common.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
// TODO: Replace this base class with a generic event envelope once services switch to type-based routing.
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseEvent {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant publishedAt = Instant.now();
    private UUID correlationId;
    private String type;
    private String actor;
}
