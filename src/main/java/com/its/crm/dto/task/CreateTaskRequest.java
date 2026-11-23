package com.its.crm.dto.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.its.crm.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    private UUID pipelineId;

    @NotBlank(message = "Step ID is required")
    private String stepId;

    private JsonNode formData;

    private JsonNode formFieldsSnapshot;

    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;
}
