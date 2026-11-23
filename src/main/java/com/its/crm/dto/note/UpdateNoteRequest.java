package com.its.crm.dto.note;

import com.its.crm.enums.RelatedEntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {

    private String title;
    private String content;
    private List<String> tags;
    private String relatedEntityId;
    private RelatedEntityType relatedEntityType;
}
