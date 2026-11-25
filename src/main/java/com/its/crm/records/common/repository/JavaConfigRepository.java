package com.its.crm.records.common.repository;

import com.its.crm.records.common.entity.JavaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JavaConfigRepository extends JpaRepository<JavaConfig, String> {
    Optional<JavaConfig> findByKey(String key);
}
