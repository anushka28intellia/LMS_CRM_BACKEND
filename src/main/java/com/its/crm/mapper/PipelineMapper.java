package com.its.crm.mapper;

import com.its.crm.dto.pipeline.CreatePipelineRequest;
import com.its.crm.dto.pipeline.PipelineDTO;
import com.its.crm.dto.pipeline.UpdatePipelineRequest;
import com.its.crm.entity.PipelineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PipelineMapper {

    PipelineDTO toDTO(PipelineEntity entity);

    @Mapping(target = "id", ignore = true)
    PipelineEntity toEntity(CreatePipelineRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UpdatePipelineRequest request, @MappingTarget PipelineEntity entity);
}
