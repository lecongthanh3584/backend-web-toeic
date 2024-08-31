package com.backend.spring.mapper;

import com.backend.spring.entity.Note;
import com.backend.spring.payload.response.NoteResponse;

public class NoteMapper {
    public static NoteResponse mapFromEntityToResponse(Note note) {
        if(note == null) {
            return null;
        }

        return new NoteResponse (
                note.getNoteId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                UserMapper.mapFromEntityToResponse(note.getUser()) //map user sang userResponse
        );
    }
}
