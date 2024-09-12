package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.GrammarContentMapper;
import com.backend.spring.entity.GrammarContent;
import com.backend.spring.payload.request.GrammarContentRequest;
import com.backend.spring.payload.response.GrammarContentResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.GrammarContentService.IGrammarContentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class GrammarContentController {

    @Autowired
    private IGrammarContentService iGrammarContentService;

    @GetMapping("/public/grammar-content/get-all")
    public ResponseEntity<?> getAllGrammarContents() {
        List<GrammarContentResponse> grammarContents = iGrammarContentService.getAllGrammarContents().stream().map(
                GrammarContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, grammarContents),
                HttpStatus.OK);
    }

    @GetMapping("/admin/grammar-content/get-by-id/{id}")
    public ResponseEntity<?> getGrammarContentById(@PathVariable("id") @Min(1) Integer id) {
        GrammarContentResponse grammarContent = GrammarContentMapper.mapFromEntityToResponse(iGrammarContentService.getGrammarContentById(id));

        if (grammarContent != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, grammarContent),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.GrammarContent.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/grammar-content/create")
    public ResponseEntity<?> createGrammarContent(@RequestBody @Valid GrammarContentRequest grammarContentRequest) {
        GrammarContent createdGrammarContent = iGrammarContentService.createGrammarContent(grammarContentRequest);

        if (createdGrammarContent != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.GrammarContent.ADD_NEW_SUCCESS),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.GrammarContent.ADD_NEW_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/grammar-content/update")
    public ResponseEntity<?> updateGrammarContent(@RequestBody @Valid GrammarContentRequest grammarContentRequest) {
        GrammarContent updatedGrammarContent = iGrammarContentService.updateGrammarContent(grammarContentRequest);

        if (updatedGrammarContent != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.GrammarContent.UPDATE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.GrammarContent.UPDATE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/grammar-content/delete/{id}")
    public ResponseEntity<?> deleteGrammarContent(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iGrammarContentService.deleteGrammarContent(id);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.GrammarContent.DELETE_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.GrammarContent.DELETE_FAILED),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/admin/grammar-content/update-status/{id}")
    public ResponseEntity<?> updateGrammarContentStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
       GrammarContent grammarContent = iGrammarContentService.updateGrammarContentStatus(id, newStatus);

       if(grammarContent != null) {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.GrammarContent.UPDATE_STATUS_SUCCESS),
                   HttpStatus.OK);

       } else {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.GrammarContent.UPDATE_STATUS_FAILED),
                   HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/admin/grammar-content/upload")
    public ResponseEntity<?> uploadGrammarContentFromExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.FILE_REQUIRED.getValue(), MessageConstant.GrammarContent.FILE_IS_REQUIRED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            iGrammarContentService.uploadGrammarContentFromExcel(file);
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_SUCCESS.getValue(), MessageConstant.GrammarContent.UPLOAD_SUCCESS),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPLOAD_FILE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy danh sách nội dung ngữ pháp theo grammar_id
    @GetMapping("/admin/grammar-content/get-content-by-grammar/{grammarId}")
    public ResponseEntity<?> getGrammarContentsByGrammarId(@PathVariable("grammarId") @Min(1) Integer grammarId) {
        List<GrammarContentResponse> grammarContents = iGrammarContentService.getGrammarContentsByGrammarId(grammarId).stream().map(
                GrammarContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!grammarContents.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, grammarContents),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.GrammarContent.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

//  Người dùng
    @GetMapping("/public/grammar-content/get-content-by-grammar/{grammarId}/enable")
    public ResponseEntity<?> getEnableGrammarContentsByGrammarId(@PathVariable("grammarId") @Min(1) Integer grammarId) {
        List<GrammarContentResponse> grammarContents = iGrammarContentService.getGrammarContentsByGrammarId(grammarId).stream().map(
                GrammarContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các GrammarContent có grammarContentStatus là 1
        List<GrammarContentResponse> filteredGrammarContents = grammarContents.stream()
                .filter(grammarContent -> grammarContent.getGrammarContentStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredGrammarContents.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, filteredGrammarContents),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.GrammarContent.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }
}
