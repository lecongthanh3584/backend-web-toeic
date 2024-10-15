package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.SectionMapper;
import com.backend.spring.entities.Section;
import com.backend.spring.payload.request.SectionRequest;
import com.backend.spring.payload.response.SectionResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.Section.ISectionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class SectionController {

    private final ISectionService iSectionService;

//  Admin
    @GetMapping("/admin/section/get-all")
    public ResponseEntity<?> getAllSections() {
        List<SectionResponse> sectionList = iSectionService.getAllSections().stream().map(
                SectionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Section.GET_DATA_SUCCESS, sectionList),
                HttpStatus.OK);
    }

//  Người dùng
    @GetMapping("/public/section/get-all-enable")
    public ResponseEntity<?> getAllEnableSections() {
        List<SectionResponse> sectionList = iSectionService.getAllSections().stream().map(
                SectionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các section có status là 1
        List<SectionResponse> filteredSectionList = sectionList.stream()
                .filter(section -> section.getStatus() == EStatus.ENABLE.getValue())
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Section.GET_DATA_SUCCESS, filteredSectionList),
                HttpStatus.OK);
    }

    @GetMapping("/admin/section/get-by-id/{id}")
    public ResponseEntity<?> getSectionById(@PathVariable("id") @Min(1) Integer id) {
        SectionResponse section = SectionMapper.mapFromEntityToResponse(iSectionService.getSectionById(id));

        if (section != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Section.GET_DATA_SUCCESS, section),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Section.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/section/get-section-name-by-id/{id}")
    public ResponseEntity<?> getSectionNameById(@PathVariable("id") @Min(1) Integer sectionId) {
        String sectionName = iSectionService.getSectionNameById(sectionId);

        if (sectionName != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Section.GET_DATA_SUCCESS, sectionName),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.Section.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/section/create")
    public ResponseEntity<?> createSection(@ModelAttribute @Valid SectionRequest sectionRequest) {
        // Kiểm tra xem tên section đã tồn tại chưa
        if (iSectionService.isSectionNameExists(sectionRequest.getName())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_FIELD_EXIST.getValue(), MessageConstant.Section.SECTION_NAME_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Section createdSection = iSectionService.createSection(sectionRequest);

            if(createdSection != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.Section.CREATE_SUCCESS),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.Section.CREATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/section/update")
    public ResponseEntity<?> updateSection(@ModelAttribute @Valid SectionRequest sectionRequest) {
        // Kiểm tra trùng lặp tên section (nếu tên đã thay đổi)
        if (iSectionService.isSectionNameExistsAndIdNot(sectionRequest.getName(), sectionRequest.getSectionId())) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_FIELD_EXIST.getValue(), MessageConstant.Section.SECTION_NAME_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            Section updatedSection = iSectionService.updateSection(sectionRequest);

            if (updatedSection != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Section.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Section.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/section/delete/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable("id") @Min(1) Integer id) {
       try {
           boolean result = iSectionService.deleteSection(id);

           if(result) {
               return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.Section.DELETE_SUCCESS),
                       HttpStatus.OK);
           } else {
               return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.Section.DELETE_FAILED),
                       HttpStatus.BAD_REQUEST);
           }
       } catch (IOException e) {
           return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), e.getMessage()),
                   HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @PutMapping("/admin/section/update-status/{id}")
    public ResponseEntity<?> updateSectionStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
        Section updateSection = iSectionService.updateSectionStatus(id, newStatus);

        if(updateSection != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.Section.UPDATE_STATUS_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.Section.UPDATE_STATUS_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }



}
