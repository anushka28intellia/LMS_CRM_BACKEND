package com.its.crm.repository;

import com.its.crm.entity.PipelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PipelineRepository extends JpaRepository<PipelineEntity, UUID> {
}
