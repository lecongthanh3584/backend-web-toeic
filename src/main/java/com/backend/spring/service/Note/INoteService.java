package com.backend.spring.service.Note;

import com.backend.spring.entity.Note;
import com.backend.spring.payload.request.NoteRequest;

import java.util.List;

public interface INoteService {
    Note createNote(NoteRequest noteRequest);
    Note updateNote(NoteRequest noteRequest);
    boolean deleteNote(Integer noteId);
    Note getNoteById(Integer noteId);
    List<Note> getAllNotes();
    List<Note> getAllNotesByUserId(Integer userId);
}
