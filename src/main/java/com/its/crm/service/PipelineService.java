package com.its.crm.service;

import com.its.crm.dto.pipeline.CreatePipelineRequest;
import com.its.crm.dto.pipeline.PipelineDTO;
import com.its.crm.dto.pipeline.UpdatePipelineRequest;

import java.util.List;
import java.util.UUID;

public interface PipelineService {

    List<PipelineDTO> getAllPipelines();

    PipelineDTO getPipelineById(UUID id);

    PipelineDTO createPipeline(CreatePipelineRequest request);

    PipelineDTO updatePipeline(UUID id, UpdatePipelineRequest request);

    void deletePipeline(UUID id);
}
