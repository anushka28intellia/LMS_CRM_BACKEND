package com.its.crm.dto.globaltask;

import com.its.crm.enums.RelatedEntityType;
import com.its.crm.enums.TaskPriority;
import com.its.crm.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
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
public class CreateGlobalTaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private UUID assignedTo;

    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @Builder.Default
    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDateTime dueDate;

    private String relatedEntityId;

    private RelatedEntityType relatedEntityType;
}
