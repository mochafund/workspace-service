package com.mochafund.workspaceservice.kafka;

import com.mochafund.workspaceservice.common.events.BaseEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public void send(BaseEvent event) {
        BaseEvent eventToSend = event;

        if (event.getActor() == null) {
            String actor = getCurrentActor();
            eventToSend = setActorOnEvent(eventToSend, actor);
        }

        if (eventToSend.getCorrelationId() == null) {
            UUID correlationId = getCurrentCorrelationId();
            eventToSend = setCorrelationIdOnEvent(eventToSend, correlationId);
        }

        eventPublisher.publishEvent(eventToSend);
        log.info("Scheduled {} event for post-commit publishing", eventToSend.getType());
    }

    private BaseEvent setActorOnEvent(BaseEvent event, String actor) {
        try {
            // Get the concrete builder class
            var builderMethod = event.getClass().getMethod("toBuilder");
            builderMethod.setAccessible(true);
            var builder = builderMethod.invoke(event);

            // Set actor on the builder
            var actorMethod = builder.getClass().getMethod("actor", String.class);
            actorMethod.setAccessible(true);
            actorMethod.invoke(builder, actor);

            // Build and return
            var buildMethod = builder.getClass().getMethod("build");
            buildMethod.setAccessible(true);
            return (BaseEvent) buildMethod.invoke(builder);
        } catch (Exception e) {
            log.warn("Failed to set actor on event, using original event: {}", e.getMessage());
            return event;
        }
    }

    private BaseEvent setCorrelationIdOnEvent(BaseEvent event, UUID correlationId) {
        try {
            // Get the concrete builder class
            var builderMethod = event.getClass().getMethod("toBuilder");
            builderMethod.setAccessible(true);
            var builder = builderMethod.invoke(event);

            // Set correlation ID on the builder
            var correlationIdMethod = builder.getClass().getMethod("correlationId", UUID.class);
            correlationIdMethod.setAccessible(true);
            correlationIdMethod.invoke(builder, correlationId);

            // Build and return
            var buildMethod = builder.getClass().getMethod("build");
            buildMethod.setAccessible(true);
            return (BaseEvent) buildMethod.invoke(builder);
        } catch (Exception e) {
            log.warn("Failed to set correlation ID on event, using original event: {}", e.getMessage());
            return event;
        }
    }

    private String getCurrentActor() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                if (auth instanceof JwtAuthenticationToken jwtAuth) {
                    Jwt jwt = jwtAuth.getToken();
                    String email = jwt.getClaimAsString("preferred_username");
                    if (email != null && !email.isBlank()) {
                        return email;
                    }
                }
                return auth.getName();
            }
        } catch (Exception e) {
            log.debug("Unable to get current user context: {}", e.getMessage());
        }
        return "SYSTEM";
    }

    private UUID getCurrentCorrelationId() {
        try {
            String correlationId = MDC.get("correlationId");
            if (correlationId != null && !correlationId.isBlank()) {
                return UUID.fromString(correlationId);
            }
        } catch (Exception e) {
            log.debug("Unable to get correlation ID from MDC: {}", e.getMessage());
        }
        return UUID.randomUUID();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    private void handleEvent(BaseEvent event) {
        kafkaTemplate.send(event.getType(), event);
        log.info("Published {} event to Kafka after transaction commit", event.getType());
    }
}
