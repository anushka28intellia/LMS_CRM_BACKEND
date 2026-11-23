package com.its.crm.dto.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineDTO {

    private UUID id;
    private String name;
    private String description;
    private JsonNode steps;
    private JsonNode deletedSteps;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
