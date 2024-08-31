package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class GrammarContentController {

    @Autowired
    private IGrammarContentService iGrammarContentService;

    @GetMapping("/public/grammar-content/get-all")
    public ResponseData<List<GrammarContentResponse>> getAllGrammarContents() {
        List<GrammarContentResponse> grammarContents = iGrammarContentService.getAllGrammarContents().stream().map(
                GrammarContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, grammarContents);
    }

    @GetMapping("/admin/grammar-content/get-by-id/{id}")
    public ResponseData<GrammarContentResponse> getGrammarContentById(@PathVariable("id") @Min(1) Integer id) {
        GrammarContentResponse grammarContent = GrammarContentMapper.mapFromEntityToResponse(iGrammarContentService.getGrammarContentById(id));

        if (grammarContent != null) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, grammarContent);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.GrammarContent.DATA_NOT_FOUND);
        }
    }

    @PostMapping("/admin/grammar-content/create")
    public ResponseData<?> createGrammarContent(@RequestBody @Valid GrammarContentRequest grammarContentRequest) {
        GrammarContent createdGrammarContent = iGrammarContentService.createGrammarContent(grammarContentRequest);

        if (createdGrammarContent != null) {
            return new ResponseData<>(HttpStatus.CREATED.value(), MessageConstant.GrammarContent.ADD_NEW_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.GrammarContent.ADD_NEW_FAILED);
        }
    }

    @PutMapping("/admin/grammar-content/update")
    public ResponseData<?> updateGrammarContent(@RequestBody @Valid GrammarContentRequest grammarContentRequest) {
        GrammarContent updatedGrammarContent = iGrammarContentService.updateGrammarContent(grammarContentRequest);

        if (updatedGrammarContent != null) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.GrammarContent.UPDATE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.GrammarContent.UPDATE_FAILED);
        }
    }

    @DeleteMapping("/admin/grammar-content/delete/{id}")
    public ResponseData<?> deleteGrammarContent(@PathVariable("id") @Min(1) Integer id) {
        boolean result = iGrammarContentService.deleteGrammarContent(id);

        if(result) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.GrammarContent.DELETE_SUCCESS);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.GrammarContent.DELETE_FAILED);
        }

    }

    @PutMapping("/admin/grammar-content/update-status/{id}")
    public ResponseData<?> updateGrammarContentStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
       GrammarContent grammarContent = iGrammarContentService.updateGrammarContentStatus(id, newStatus);

       if(grammarContent != null) {
           return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.GrammarContent.UPDATE_STATUS_SUCCESS);
       } else {
           return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.GrammarContent.UPDATE_STATUS_FAILED);
       }
    }

    @PostMapping("/admin/grammar-content/upload")
    public ResponseData<?> uploadGrammarContentFromExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.GrammarContent.FILE_IS_REQUIRED);
        }

        try {
            iGrammarContentService.uploadGrammarContentFromExcel(file);
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.GrammarContent.UPLOAD_SUCCESS);

        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    // Lấy danh sách nội dung ngữ pháp theo grammar_id
    @GetMapping("/admin/grammar-content/get-content-by-grammar/{grammarId}")
    public ResponseData<List<GrammarContentResponse>> getGrammarContentsByGrammarId(@PathVariable("grammarId") @Min(1) Integer grammarId) {
        List<GrammarContentResponse> grammarContents = iGrammarContentService.getGrammarContentsByGrammarId(grammarId).stream().map(
                GrammarContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        if (!grammarContents.isEmpty()) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, grammarContents);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.GrammarContent.DATA_NOT_FOUND);
        }
    }

//  Người dùng
    @GetMapping("/public/grammar-content/get-content-by-grammar/{grammarId}/enable")
    public ResponseData<List<GrammarContentResponse>> getEnableGrammarContentsByGrammarId(@PathVariable("grammarId") @Min(1) Integer grammarId) {
        List<GrammarContentResponse> grammarContents = iGrammarContentService.getGrammarContentsByGrammarId(grammarId).stream().map(
                GrammarContentMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các GrammarContent có grammarContentStatus là 1
        List<GrammarContentResponse> filteredGrammarContents = grammarContents.stream()
                .filter(grammarContent -> grammarContent.getGrammarContentStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredGrammarContents.isEmpty()) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.GrammarContent.GET_DATA_SUCCESS, filteredGrammarContents);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.GrammarContent.DATA_NOT_FOUND);
        }
    }
}
