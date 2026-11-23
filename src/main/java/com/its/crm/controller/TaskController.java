package com.its.crm.controller;

import com.its.crm.dto.task.CreateTaskRequest;
import com.its.crm.dto.task.TaskDTO;
import com.its.crm.dto.task.UpdateTaskRequest;
import com.its.crm.service.TaskService;
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
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing pipeline tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/pipelines/{pipelineId}/tasks")
    @Operation(summary = "Get all tasks for a specific pipeline")
    public ResponseEntity<List<TaskDTO>> getTasksByPipelineId(@PathVariable UUID pipelineId) {
        return ResponseEntity.ok(taskService.getTasksByPipelineId(pipelineId));
    }

    @PostMapping("/pipelines/{pipelineId}/tasks")
    @Operation(summary = "Create a task in a pipeline")
    public ResponseEntity<TaskDTO> createTask(
            @PathVariable UUID pipelineId,
            @Valid @RequestBody CreateTaskRequest request) {
        TaskDTO created = taskService.createTask(pipelineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Update task by ID")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable UUID id, @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Delete task by ID")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
