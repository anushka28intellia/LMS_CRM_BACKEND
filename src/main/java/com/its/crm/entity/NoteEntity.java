package com.its.crm.entity;

import com.its.crm.enums.RelatedEntityType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes", indexes = {
        @Index(name = "idx_notes_created_by", columnList = "created_by_id"),
        @Index(name = "idx_notes_related", columnList = "related_entity_id, related_entity_type")
})
public class NoteEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private UserEntity createdByUser;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> tags;

    @Column(name = "related_entity_id")
    private String relatedEntityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "related_entity_type", length = 50)
    private RelatedEntityType relatedEntityType;
}
