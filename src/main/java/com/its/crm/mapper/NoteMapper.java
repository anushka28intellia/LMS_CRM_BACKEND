package com.its.crm.mapper;

import com.its.crm.dto.note.CreateNoteRequest;
import com.its.crm.dto.note.NoteDTO;
import com.its.crm.dto.note.UpdateNoteRequest;
import com.its.crm.entity.NoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NoteMapper {

    @Mapping(source = "createdByUser.id", target = "createdById")
    @Mapping(source = "createdByUser.name", target = "createdByName")
    NoteDTO toDTO(NoteEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    NoteEntity toEntity(CreateNoteRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    void updateEntityFromDTO(UpdateNoteRequest request, @MappingTarget NoteEntity entity);
}
