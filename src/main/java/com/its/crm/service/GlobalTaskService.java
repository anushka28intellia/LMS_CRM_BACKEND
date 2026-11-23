package com.its.crm.service;

import com.its.crm.dto.globaltask.CreateGlobalTaskRequest;
import com.its.crm.dto.globaltask.GlobalTaskDTO;
import com.its.crm.dto.globaltask.UpdateGlobalTaskRequest;
import com.its.crm.enums.TaskPriority;
import com.its.crm.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface GlobalTaskService {

    List<GlobalTaskDTO> getAllGlobalTasks(TaskStatus status, TaskPriority priority, UUID assignedTo);

    GlobalTaskDTO getGlobalTaskById(UUID id);

    GlobalTaskDTO createGlobalTask(CreateGlobalTaskRequest request);

    GlobalTaskDTO updateGlobalTask(UUID id, UpdateGlobalTaskRequest request);

    void deleteGlobalTask(UUID id);
}
