package com.its.crm.dto.note;

import com.its.crm.enums.RelatedEntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {

    private UUID id;
    private String title;
    private String content;
    private UUID createdById;
    private String createdByName;
    private List<String> tags;
    private String relatedEntityId;
    private RelatedEntityType relatedEntityType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
