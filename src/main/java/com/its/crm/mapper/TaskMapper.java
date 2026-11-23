package com.its.crm.mapper;

import com.its.crm.dto.task.CreateTaskRequest;
import com.its.crm.dto.task.TaskDTO;
import com.its.crm.dto.task.UpdateTaskRequest;
import com.its.crm.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(source = "pipeline.id", target = "pipelineId")
    TaskDTO toDTO(TaskEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pipeline", ignore = true)
    TaskEntity toEntity(CreateTaskRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pipeline", ignore = true)
    void updateEntityFromDTO(UpdateTaskRequest request, @MappingTarget TaskEntity entity);
}
