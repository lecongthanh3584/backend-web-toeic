package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.NoteMapper;
import com.backend.spring.entity.Note;
import com.backend.spring.payload.request.NoteRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.NoteResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.Note.INoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class NoteController {

    @Autowired
    private INoteService iNoteService;

    @PostMapping("/user/note/create")
    public ResponseEntity<?> createNote(@RequestBody @Valid NoteRequest noteRequest) {
        Note createdNote = iNoteService.createNote(noteRequest);

        if(createdNote != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Note.CREATE_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Note.CREATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/user/note/update")
    public ResponseEntity<?> updateNote(@RequestBody @Valid NoteRequest noteRequest) {
        Note note = iNoteService.updateNote(noteRequest);

        if(note != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Note.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Note.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/note/delete/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable("noteId") @Min(1) Integer noteId) {
        boolean result = iNoteService.deleteNote(noteId);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Note.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Note.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/note/get-by-id/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable("id") @Min(1) Integer noteId) {
        NoteResponse note = NoteMapper.mapFromEntityToResponse(iNoteService.getNoteById(noteId));

        if (note != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Note.GET_DATA_SUCCESS, note),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Note.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/note/get-all")
    public ResponseEntity<?> getAllNotes() {
        List<NoteResponse> noteList = iNoteService.getAllNotes().stream().map(
                NoteMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Note.GET_DATA_SUCCESS, noteList),
                HttpStatus.OK);
    }

    @GetMapping("/user/note/get-note-by-user/{userId}")
    public ResponseEntity<?> getAllNotesByUserId(@PathVariable("userId") @Min(1) Integer userId) {
        List<NoteResponse> noteList = iNoteService.getAllNotesByUserId(userId).stream().map(
                NoteMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!noteList.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Note.GET_DATA_SUCCESS, noteList),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Note.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }
}
