package com.its.crm.controller;

import com.its.crm.dto.globaltask.CreateGlobalTaskRequest;
import com.its.crm.dto.globaltask.GlobalTaskDTO;
import com.its.crm.dto.globaltask.UpdateGlobalTaskRequest;
import com.its.crm.enums.TaskPriority;
import com.its.crm.enums.TaskStatus;
import com.its.crm.service.GlobalTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/global-tasks")
@RequiredArgsConstructor
@Tag(name = "Global Tasks", description = "Endpoints for managing standalone global tasks")
public class GlobalTaskController {

    private final GlobalTaskService globalTaskService;

    @GetMapping
    @Operation(summary = "Get all global tasks with optional filters")
    public ResponseEntity<List<GlobalTaskDTO>> getAllGlobalTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) UUID assignedTo) {
        return ResponseEntity.ok(globalTaskService.getAllGlobalTasks(status, priority, assignedTo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get global task by ID")
    public ResponseEntity<GlobalTaskDTO> getGlobalTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(globalTaskService.getGlobalTaskById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new global task")
    public ResponseEntity<GlobalTaskDTO> createGlobalTask(@Valid @RequestBody CreateGlobalTaskRequest request) {
        GlobalTaskDTO created = globalTaskService.createGlobalTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update global task by ID")
    public ResponseEntity<GlobalTaskDTO> updateGlobalTask(@PathVariable UUID id, @RequestBody UpdateGlobalTaskRequest request) {
        return ResponseEntity.ok(globalTaskService.updateGlobalTask(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete global task by ID")
    public ResponseEntity<Void> deleteGlobalTask(@PathVariable UUID id) {
        globalTaskService.deleteGlobalTask(id);
        return ResponseEntity.noContent().build();
    }
}
