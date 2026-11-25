package com.its.crm.config;

import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * The Jackson 3 mapper Hypersistence {@code JsonType} uses for this module's JSONB columns
 * (pipeline steps, task form data, note tags).
 * <p>
 * Hypersistence for Hibernate 7 (Spring Boot 4) is Jackson 3 only, while the entities hold Jackson 2
 * {@code com.fasterxml.jackson.databind.JsonNode} values. Left alone, Jackson 3 writes such a node as a bean
 * (<code>{"array":true,"empty":false,...}</code>) and cannot read it back. This mapper converts those nodes through their
 * JSON text in both directions, so the stored JSON is exactly the node's content. Registered by
 * {@code hypersistence.utils.jackson.object.mapper} in application.properties.
 */
public class HypersistenceJsonMapperSupplier implements ObjectMapperSupplier {

    private static final com.fasterxml.jackson.databind.ObjectMapper JACKSON2 =
            new com.fasterxml.jackson.databind.ObjectMapper();

    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .addModule(new SimpleModule("jackson2-json-node")
                    .addSerializer(com.fasterxml.jackson.databind.JsonNode.class, new Jackson2NodeSerializer())
                    .addDeserializer(com.fasterxml.jackson.databind.JsonNode.class, new Jackson2NodeDeserializer()))
            .build();

    @Override
    public ObjectMapper get() {
        return MAPPER;
    }

    /** Writes a Jackson 2 node as the same JSON tree. */
    static final class Jackson2NodeSerializer extends ValueSerializer<com.fasterxml.jackson.databind.JsonNode> {
        @Override
        public void serialize(com.fasterxml.jackson.databind.JsonNode value, JsonGenerator gen, SerializationContext ctxt)
                throws JacksonException {
            gen.writeTree(MAPPER.readTree(value.toString()));
        }
    }

    /** Reads any JSON value into a Jackson 2 node. */
    static final class Jackson2NodeDeserializer extends ValueDeserializer<com.fasterxml.jackson.databind.JsonNode> {
        @Override
        public com.fasterxml.jackson.databind.JsonNode deserialize(JsonParser p, DeserializationContext ctxt)
                throws JacksonException {
            String json = ctxt.readTree(p).toString();
            try {
                return JACKSON2.readTree(json);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new IllegalStateException("Stored JSON could not be read as a Jackson 2 node", e);
            }
        }
    }
}
