package com.its.crm.repository;

import com.its.crm.entity.TaskEntity;
import com.its.crm.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findByPipelineId(UUID pipelineId);

    List<TaskEntity> findByPipelineIdAndStatus(UUID pipelineId, TaskStatus status);

    List<TaskEntity> findByPipelineIdAndStepId(UUID pipelineId, String stepId);
}
