package com.its.crm.service.impl;

import com.its.crm.dto.note.CreateNoteRequest;
import com.its.crm.dto.note.NoteDTO;
import com.its.crm.dto.note.UpdateNoteRequest;
import com.its.crm.entity.NoteEntity;
import com.its.crm.entity.UserEntity;
import com.its.crm.enums.RelatedEntityType;
import com.its.crm.exception.ResourceNotFoundException;
import com.its.crm.mapper.NoteMapper;
import com.its.crm.repository.NoteRepository;
import com.its.crm.repository.UserRepository;
import com.its.crm.security.SecurityUtils;
import com.its.crm.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NoteDTO> getAllNotes(String relatedEntityId, RelatedEntityType relatedEntityType) {
        if (StringUtils.isNotBlank(relatedEntityId) && relatedEntityType != null) {
            return noteRepository.findByRelatedEntityIdAndRelatedEntityType(relatedEntityId, relatedEntityType)
                    .stream()
                    .map(noteMapper::toDTO)
                    .toList();
        }
        return noteRepository.findAll().stream()
                .map(noteMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NoteDTO getNoteById(UUID id) {
        return noteRepository.findById(id)
                .map(noteMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));
    }

    @Override
    @Transactional
    public NoteDTO createNote(CreateNoteRequest request) {
        NoteEntity entity = noteMapper.toEntity(request);

        SecurityUtils.getCurrentUserId().ifPresent(userId -> {
            UserEntity user = userRepository.findById(userId).orElse(null);
            entity.setCreatedByUser(user);
        });

        NoteEntity saved = noteRepository.save(entity);
        return noteMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public NoteDTO updateNote(UUID id, UpdateNoteRequest request) {
        NoteEntity note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));

        noteMapper.updateEntityFromDTO(request, note);
        NoteEntity updated = noteRepository.save(note);
        return noteMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteNote(UUID id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note", "id", id);
        }
        noteRepository.deleteById(id);
    }
}
