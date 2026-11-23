package com.its.crm.entity;

import com.its.crm.enums.RelatedEntityType;
import com.its.crm.enums.TaskPriority;
import com.its.crm.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "global_tasks", indexes = {
        @Index(name = "idx_global_tasks_assigned", columnList = "assigned_to_id"),
        @Index(name = "idx_global_tasks_created_by", columnList = "created_by_id"),
        @Index(name = "idx_global_tasks_status", columnList = "status"),
        @Index(name = "idx_global_tasks_priority", columnList = "priority"),
        @Index(name = "idx_global_tasks_due", columnList = "due_date")
})
public class GlobalTaskEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private UserEntity assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private UserEntity createdByUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskPriority priority;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "related_entity_id")
    private String relatedEntityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "related_entity_type", length = 50)
    private RelatedEntityType relatedEntityType;
}
