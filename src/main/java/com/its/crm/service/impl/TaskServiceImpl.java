package com.its.crm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.crm.dto.task.CreateTaskRequest;
import com.its.crm.dto.task.TaskDTO;
import com.its.crm.dto.task.UpdateTaskRequest;
import com.its.crm.entity.PipelineEntity;
import com.its.crm.entity.TaskEntity;
import com.its.crm.enums.TaskStatus;
import com.its.crm.exception.ResourceNotFoundException;
import com.its.crm.mapper.TaskMapper;
import com.its.crm.repository.PipelineRepository;
import com.its.crm.repository.TaskRepository;
import com.its.crm.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PipelineRepository pipelineRepository;
    private final TaskMapper taskMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByPipelineId(UUID pipelineId) {
        if (!pipelineRepository.existsById(pipelineId)) {
            throw new ResourceNotFoundException("Pipeline", "id", pipelineId);
        }
        return taskRepository.findByPipelineId(pipelineId).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(UUID id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }

    @Override
    @Transactional
    public TaskDTO createTask(UUID pipelineId, CreateTaskRequest request) {
        PipelineEntity pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline", "id", pipelineId));

        TaskEntity task = taskMapper.toEntity(request);
        task.setPipeline(pipeline);
        if (task.getFormData() == null) {
            task.setFormData(objectMapper.createObjectNode());
        }
        if (task.getFormFieldsSnapshot() == null) {
            task.setFormFieldsSnapshot(objectMapper.createArrayNode());
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }

        TaskEntity saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public TaskDTO updateTask(UUID id, UpdateTaskRequest request) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        taskMapper.updateEntityFromDTO(request, task);
        TaskEntity updated = taskRepository.save(task);
        return taskMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        taskRepository.deleteById(id);
    }
}
