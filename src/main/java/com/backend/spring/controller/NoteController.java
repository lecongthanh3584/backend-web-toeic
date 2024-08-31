package com.backend.spring.controller;

import com.backend.spring.mapper.NoteMapper;
import com.backend.spring.entity.Note;
import com.backend.spring.payload.request.NoteRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.NoteResponse;
import com.backend.spring.service.Note.INoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class NoteController {

    @Autowired
    private INoteService iNoteService;

    @PostMapping("/user/note/create")
    public ResponseEntity<MessageResponse> createNote(@RequestBody @Valid NoteRequest noteRequest) {
        Note createdNote = iNoteService.createNote(noteRequest);

        if(createdNote != null) {
            return ResponseEntity.ok(new MessageResponse("Tạo mới ghi chú thành công"));
        } else {
            return new ResponseEntity<>(new MessageResponse("Tạo mới ghi chú thất bại"), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/user/note/update")
    public ResponseEntity<MessageResponse> updateNote(@RequestBody @Valid NoteRequest noteRequest) {
        Note note = iNoteService.updateNote(noteRequest);

        if(note != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật ghi chú thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật ghi chú thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/note/delete/{noteId}")
    public ResponseEntity<MessageResponse> deleteNote(@PathVariable Integer noteId) {
        boolean result = iNoteService.deleteNote(noteId);

        if(result) {
            return new ResponseEntity<>(new MessageResponse("Xoá ghi chú thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Xoá ghi chú thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/note/get-by-id/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable("id") Integer noteId) {
        NoteResponse note = NoteMapper.mapFromEntityToResponse(iNoteService.getNoteById(noteId));

        if (note != null) {
            return new ResponseEntity<>(note, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/note/get-all")
    public ResponseEntity<List<NoteResponse>> getAllNotes() {
        List<NoteResponse> noteList = iNoteService.getAllNotes().stream().map(
                NoteMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

    @GetMapping("/user/note/get-note-by-user/{userId}")
    public ResponseEntity<List<NoteResponse>> getAllNotesByUserId(@PathVariable Integer userId) {
        List<NoteResponse> noteList = iNoteService.getAllNotesByUserId(userId).stream().map(
                NoteMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!noteList.isEmpty()) {
            return new ResponseEntity<>(noteList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(noteList, HttpStatus.NOT_FOUND);
        }
    }
}
