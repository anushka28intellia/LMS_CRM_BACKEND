package com.its.crm.repository;

import com.its.crm.entity.GlobalTaskEntity;
import com.its.crm.enums.TaskPriority;
import com.its.crm.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GlobalTaskRepository extends JpaRepository<GlobalTaskEntity, UUID> {

    List<GlobalTaskEntity> findByAssignedToId(UUID userId);

    List<GlobalTaskEntity> findByCreatedByUserId(UUID userId);

    List<GlobalTaskEntity> findByStatus(TaskStatus status);

    List<GlobalTaskEntity> findByPriority(TaskPriority priority);
}
