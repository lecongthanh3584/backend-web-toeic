package com.backend.spring.service.Note;

import com.backend.spring.entity.User;
import com.backend.spring.entity.Note;
import com.backend.spring.payload.request.NoteRequest;
import com.backend.spring.repository.NoteRepository;
import com.backend.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class NoteService implements INoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Note createNote(NoteRequest noteRequest) {
        Optional<User> userOptional = userRepository.findById(noteRequest.getUserId());

        if (userOptional.isPresent()) {
            Note note = new Note();

            note.setUser(userOptional.get());
            note.setTitle(noteRequest.getTitle());
            note.setContent(noteRequest.getContent());
            note.setCreatedAt(LocalDateTime.now());
            note.setUpdatedAt(LocalDateTime.now());
            return noteRepository.save(note);
        }

        return null;
    }

    @Override
    public Note updateNote(NoteRequest noteRequest) {
        Optional<Note> noteOptional = noteRepository.findById(noteRequest.getNoteId());

        if (noteOptional.isPresent()) {
            Note existingNote = noteOptional.get();

            existingNote.setTitle(noteRequest.getTitle());
            existingNote.setContent(noteRequest.getContent());
            existingNote.setUpdatedAt(LocalDateTime.now());

            return noteRepository.save(existingNote);
        }

        return null;
    }

    @Override
    public boolean deleteNote(Integer noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if(noteOptional.isEmpty()) {
            return false;
        }

        noteRepository.deleteById(noteId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Note getNoteById(Integer noteId) {
        return noteRepository.findById(noteId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getAllNotesByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return noteRepository.findByUser(user);
        }

        return Collections.emptyList();
    }
}
