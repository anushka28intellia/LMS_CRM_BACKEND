package com.its.crm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.its.crm.dto.pipeline.CreatePipelineRequest;
import com.its.crm.dto.pipeline.PipelineDTO;
import com.its.crm.dto.pipeline.UpdatePipelineRequest;
import com.its.crm.entity.PipelineEntity;
import com.its.crm.exception.ResourceNotFoundException;
import com.its.crm.mapper.PipelineMapper;
import com.its.crm.repository.PipelineRepository;
import com.its.crm.service.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {

    private final PipelineRepository pipelineRepository;
    private final PipelineMapper pipelineMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PipelineDTO> getAllPipelines() {
        return pipelineRepository.findAll().stream()
                .map(pipelineMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PipelineDTO getPipelineById(UUID id) {
        return pipelineRepository.findById(id)
                .map(pipelineMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline", "id", id));
    }

    @Override
    @Transactional
    public PipelineDTO createPipeline(CreatePipelineRequest request) {
        PipelineEntity entity = pipelineMapper.toEntity(request);
        if (entity.getSteps() == null) {
            entity.setSteps(objectMapper.createArrayNode());
        }
        if (entity.getDeletedSteps() == null) {
            entity.setDeletedSteps(objectMapper.createArrayNode());
        }

        PipelineEntity saved = pipelineRepository.save(entity);
        return pipelineMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public PipelineDTO updatePipeline(UUID id, UpdatePipelineRequest request) {
        PipelineEntity pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline", "id", id));

        pipelineMapper.updateEntityFromDTO(request, pipeline);
        PipelineEntity updated = pipelineRepository.save(pipeline);
        return pipelineMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deletePipeline(UUID id) {
        if (!pipelineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pipeline", "id", id);
        }
        pipelineRepository.deleteById(id);
    }
}
