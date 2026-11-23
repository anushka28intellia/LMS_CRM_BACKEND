package com.its.crm.mapper;

import com.its.crm.dto.globaltask.CreateGlobalTaskRequest;
import com.its.crm.dto.globaltask.GlobalTaskDTO;
import com.its.crm.dto.globaltask.UpdateGlobalTaskRequest;
import com.its.crm.entity.GlobalTaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GlobalTaskMapper {

    @Mapping(source = "assignedTo.id", target = "assignedToId")
    @Mapping(source = "assignedTo.name", target = "assignedToName")
    @Mapping(source = "createdByUser.id", target = "createdById")
    @Mapping(source = "createdByUser.name", target = "createdByName")
    GlobalTaskDTO toDTO(GlobalTaskEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    GlobalTaskEntity toEntity(CreateGlobalTaskRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    void updateEntityFromDTO(UpdateGlobalTaskRequest request, @MappingTarget GlobalTaskEntity entity);
}
