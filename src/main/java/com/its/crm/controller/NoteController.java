package com.its.crm.controller;

import com.its.crm.dto.note.CreateNoteRequest;
import com.its.crm.dto.note.NoteDTO;
import com.its.crm.dto.note.UpdateNoteRequest;
import com.its.crm.enums.RelatedEntityType;
import com.its.crm.service.NoteService;
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
@RequestMapping("/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Endpoints for managing notes")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Get all notes with optional filter by related entity")
    public ResponseEntity<List<NoteDTO>> getAllNotes(
            @RequestParam(required = false) String relatedEntityId,
            @RequestParam(required = false) RelatedEntityType relatedEntityType) {
        return ResponseEntity.ok(noteService.getAllNotes(relatedEntityId, relatedEntityType));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get note by ID")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable UUID id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new note")
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody CreateNoteRequest request) {
        NoteDTO created = noteService.createNote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update note by ID")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable UUID id, @RequestBody UpdateNoteRequest request) {
        return ResponseEntity.ok(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note by ID")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
