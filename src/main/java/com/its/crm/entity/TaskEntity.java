package com.its.crm.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.its.crm.enums.TaskStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_tasks_pipeline", columnList = "pipeline_id"),
        @Index(name = "idx_tasks_status", columnList = "status"),
        @Index(name = "idx_tasks_created", columnList = "created_at DESC")
})
public class TaskEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private PipelineEntity pipeline;

    @Column(name = "step_id", nullable = false)
    private String stepId;

    @Type(JsonType.class)
    @Column(name = "form_data", columnDefinition = "jsonb", nullable = false)
    private JsonNode formData;

    @Type(JsonType.class)
    @Column(name = "form_fields_snapshot", columnDefinition = "jsonb", nullable = false)
    private JsonNode formFieldsSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskStatus status;
}
