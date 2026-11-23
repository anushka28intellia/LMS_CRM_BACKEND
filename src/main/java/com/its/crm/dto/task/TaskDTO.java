package com.its.crm.dto.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.its.crm.enums.TaskStatus;
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
public class TaskDTO {

    private UUID id;
    private UUID pipelineId;
    private String stepId;
    private JsonNode formData;
    private JsonNode formFieldsSnapshot;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
