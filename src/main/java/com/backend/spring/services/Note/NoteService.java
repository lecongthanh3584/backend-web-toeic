package com.backend.spring.services.Note;

import com.backend.spring.entities.User;
import com.backend.spring.entities.Note;
import com.backend.spring.payload.request.NoteRequest;
import com.backend.spring.repositories.NoteRepository;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class NoteService implements INoteService {

    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    @Override
    public Note createNote(NoteRequest noteRequest) {
        User userLogin = UserUtil.getDataUserLogin();

        if(userLogin != null) {
            Note note = new Note();
            note.setUser(userLogin);
            note.setTitle(noteRequest.getTitle());
            note.setContent(noteRequest.getContent());
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
    public List<Note> getAllNotesByUserId() {
        User userLogin = UserUtil.getDataUserLogin();
        if(userLogin != null) {
            return noteRepository.findByUser(userLogin);
        }

        return Collections.emptyList();
    }
}
