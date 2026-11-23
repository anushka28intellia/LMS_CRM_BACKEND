package com.its.crm.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RelatedEntityType {
    TASK("task"),
    PIPELINE("pipeline"),
    GLOBAL("global");

    private final String value;

    RelatedEntityType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RelatedEntityType fromValue(String value) {
        if (value == null) return null;
        for (RelatedEntityType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown related entity type: " + value);
    }
}
