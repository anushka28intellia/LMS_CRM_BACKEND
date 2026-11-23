package com.its.crm.controller;

import com.its.crm.dto.pipeline.CreatePipelineRequest;
import com.its.crm.dto.pipeline.PipelineDTO;
import com.its.crm.dto.pipeline.UpdatePipelineRequest;
import com.its.crm.service.PipelineService;
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
@RequestMapping("/pipelines")
@RequiredArgsConstructor
@Tag(name = "Pipelines", description = "Endpoints for managing pipelines and workflow stages")
public class PipelineController {

    private final PipelineService pipelineService;

    @GetMapping
    @Operation(summary = "Get all pipelines")
    public ResponseEntity<List<PipelineDTO>> getAllPipelines() {
        return ResponseEntity.ok(pipelineService.getAllPipelines());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pipeline by ID")
    public ResponseEntity<PipelineDTO> getPipelineById(@PathVariable UUID id) {
        return ResponseEntity.ok(pipelineService.getPipelineById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new pipeline")
    public ResponseEntity<PipelineDTO> createPipeline(@Valid @RequestBody CreatePipelineRequest request) {
        PipelineDTO created = pipelineService.createPipeline(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pipeline by ID")
    public ResponseEntity<PipelineDTO> updatePipeline(@PathVariable UUID id, @RequestBody UpdatePipelineRequest request) {
        return ResponseEntity.ok(pipelineService.updatePipeline(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pipeline by ID")
    public ResponseEntity<Void> deletePipeline(@PathVariable UUID id) {
        pipelineService.deletePipeline(id);
        return ResponseEntity.noContent().build();
    }
}
