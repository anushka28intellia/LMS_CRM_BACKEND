package com.its.crm.dto.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePipelineRequest {

    private String name;
    private String description;
    private JsonNode steps;
    private JsonNode deletedSteps;
}
