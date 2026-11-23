package com.its.crm.repository;

import com.its.crm.entity.NoteEntity;
import com.its.crm.enums.RelatedEntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {

    List<NoteEntity> findByCreatedByUserId(UUID userId);

    List<NoteEntity> findByRelatedEntityIdAndRelatedEntityType(String relatedEntityId, RelatedEntityType relatedEntityType);
}
