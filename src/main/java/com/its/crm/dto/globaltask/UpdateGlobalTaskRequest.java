package com.its.crm.dto.globaltask;

import com.its.crm.enums.RelatedEntityType;
import com.its.crm.enums.TaskPriority;
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
public class UpdateGlobalTaskRequest {

    private String title;
    private String description;
    private UUID assignedTo;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private String relatedEntityId;
    private RelatedEntityType relatedEntityType;
}
