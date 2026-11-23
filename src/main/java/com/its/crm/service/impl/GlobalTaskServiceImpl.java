package com.its.crm.service.impl;

import com.its.crm.dto.globaltask.CreateGlobalTaskRequest;
import com.its.crm.dto.globaltask.GlobalTaskDTO;
import com.its.crm.dto.globaltask.UpdateGlobalTaskRequest;
import com.its.crm.entity.GlobalTaskEntity;
import com.its.crm.entity.UserEntity;
import com.its.crm.enums.TaskPriority;
import com.its.crm.enums.TaskStatus;
import com.its.crm.exception.ResourceNotFoundException;
import com.its.crm.mapper.GlobalTaskMapper;
import com.its.crm.repository.GlobalTaskRepository;
import com.its.crm.repository.UserRepository;
import com.its.crm.security.SecurityUtils;
import com.its.crm.service.GlobalTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlobalTaskServiceImpl implements GlobalTaskService {

    private final GlobalTaskRepository globalTaskRepository;
    private final UserRepository userRepository;
    private final GlobalTaskMapper globalTaskMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GlobalTaskDTO> getAllGlobalTasks(TaskStatus status, TaskPriority priority, UUID assignedTo) {
        if (assignedTo != null) {
            return globalTaskRepository.findByAssignedToId(assignedTo).stream()
                    .map(globalTaskMapper::toDTO)
                    .toList();
        }
        if (status != null) {
            return globalTaskRepository.findByStatus(status).stream()
                    .map(globalTaskMapper::toDTO)
                    .toList();
        }
        if (priority != null) {
            return globalTaskRepository.findByPriority(priority).stream()
                    .map(globalTaskMapper::toDTO)
                    .toList();
        }
        return globalTaskRepository.findAll().stream()
                .map(globalTaskMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GlobalTaskDTO getGlobalTaskById(UUID id) {
        return globalTaskRepository.findById(id)
                .map(globalTaskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("GlobalTask", "id", id));
    }

    @Override
    @Transactional
    public GlobalTaskDTO createGlobalTask(CreateGlobalTaskRequest request) {
        GlobalTaskEntity entity = globalTaskMapper.toEntity(request);

        if (request.getAssignedTo() != null) {
            UserEntity assignee = userRepository.findById(request.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedTo()));
            entity.setAssignedTo(assignee);
        }

        SecurityUtils.getCurrentUserId().ifPresent(userId -> {
            UserEntity creator = userRepository.findById(userId).orElse(null);
            entity.setCreatedByUser(creator);
        });

        GlobalTaskEntity saved = globalTaskRepository.save(entity);
        return globalTaskMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GlobalTaskDTO updateGlobalTask(UUID id, UpdateGlobalTaskRequest request) {
        GlobalTaskEntity task = globalTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GlobalTask", "id", id));

        globalTaskMapper.updateEntityFromDTO(request, task);

        if (request.getAssignedTo() != null) {
            UserEntity assignee = userRepository.findById(request.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedTo()));
            task.setAssignedTo(assignee);
        }

        GlobalTaskEntity updated = globalTaskRepository.save(task);
        return globalTaskMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteGlobalTask(UUID id) {
        if (!globalTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("GlobalTask", "id", id);
        }
        globalTaskRepository.deleteById(id);
    }
}
