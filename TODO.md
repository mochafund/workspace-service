# Workspace Service TODO

- [ ] Replace class-name-based Kafka deserialization with a generic event envelope (type + version + payload).
- [ ] Emit envelope-based events from `KafkaProducer`, removing reflection for correlationId/actor enrichment.
- [ ] Update `WorkspaceEventConsumer` to parse envelopes and route by `type`.
- [ ] Align with identity-service on shared event type constants and schema documentation.
- [ ] Add integration tests covering envelope publish/consume flow.
