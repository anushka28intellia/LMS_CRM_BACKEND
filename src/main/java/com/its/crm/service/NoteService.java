package com.its.crm.service;

import com.its.crm.dto.note.CreateNoteRequest;
import com.its.crm.dto.note.NoteDTO;
import com.its.crm.dto.note.UpdateNoteRequest;
import com.its.crm.enums.RelatedEntityType;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    List<NoteDTO> getAllNotes(String relatedEntityId, RelatedEntityType relatedEntityType);

    NoteDTO getNoteById(UUID id);

    NoteDTO createNote(CreateNoteRequest request);

    NoteDTO updateNote(UUID id, UpdateNoteRequest request);

    void deleteNote(UUID id);
}
