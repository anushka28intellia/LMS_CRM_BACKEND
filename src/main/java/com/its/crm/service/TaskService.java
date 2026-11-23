package com.its.crm.service;

import com.its.crm.dto.task.CreateTaskRequest;
import com.its.crm.dto.task.TaskDTO;
import com.its.crm.dto.task.UpdateTaskRequest;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<TaskDTO> getTasksByPipelineId(UUID pipelineId);

    TaskDTO getTaskById(UUID id);

    TaskDTO createTask(UUID pipelineId, CreateTaskRequest request);

    TaskDTO updateTask(UUID id, UpdateTaskRequest request);

    void deleteTask(UUID id);
}
