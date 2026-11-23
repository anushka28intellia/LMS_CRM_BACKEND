package com.its.crm.dto.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.its.crm.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    private String stepId;
    private JsonNode formData;
    private JsonNode formFieldsSnapshot;
    private TaskStatus status;
}
