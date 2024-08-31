package com.backend.spring.controller;

import com.backend.spring.enums.EStatus;
import com.backend.spring.mapper.SectionMapper;
import com.backend.spring.entity.Section;
import com.backend.spring.payload.request.SectionRequest;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.SectionResponse;
import com.backend.spring.service.Section.ISectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class SectionController {

    @Autowired
    private ISectionService iSectionService;

//  Admin
    @GetMapping("/admin/section/get-all")
    public ResponseEntity<List<SectionResponse>> getAllSections() {
        List<SectionResponse> sectionList = iSectionService.getAllSections().stream().map(
                SectionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(sectionList, HttpStatus.OK);
    }

//  Người dùng
    @GetMapping("/public/section/get-all-enable")
    public ResponseEntity<List<SectionResponse>> getAllEnableSections() {
        List<SectionResponse> sectionList = iSectionService.getAllSections().stream().map(
                SectionMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các section có status là 1
        List<SectionResponse> filteredSectionList = sectionList.stream()
                .filter(section -> section.getStatus() == EStatus.ENABLE.getValue())
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredSectionList, HttpStatus.OK);
    }

    @GetMapping("/admin/section/get-by-id/{id}")
    public ResponseEntity<SectionResponse> getSectionById(@PathVariable Integer id) {
        SectionResponse section = SectionMapper.mapFromEntityToResponse(iSectionService.getSectionById(id));

        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/section/get-section-name-by-id/{id}")
    public ResponseEntity<String> getSectionNameById(@PathVariable("id") Integer sectionId) {
        String sectionName = iSectionService.getSectionNameById(sectionId);

        if (sectionName != null) {
            return new ResponseEntity<>(sectionName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/section/create")
    public ResponseEntity<MessageResponse> createSection(@ModelAttribute @Valid SectionRequest sectionRequest) {
        // Kiểm tra xem tên section đã tồn tại chưa
        if (iSectionService.isSectionNameExists(sectionRequest.getName())) {
            return new ResponseEntity<>(new MessageResponse("Tên dạng phần đã tồn tại"), HttpStatus.BAD_REQUEST);
        }

        try {
            Section createdSection = iSectionService.createSection(sectionRequest);

            if(createdSection != null) {
                return ResponseEntity.ok(new MessageResponse("Thêm dạng phần thành công!"));
            } else {
                return new ResponseEntity<>(new MessageResponse("Thêm dạng phần thất bại!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi thêm dạng phần: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/section/update")
    public ResponseEntity<MessageResponse> updateSection(@ModelAttribute @Valid SectionRequest sectionRequest) {
        // Kiểm tra trùng lặp tên section (nếu tên đã thay đổi)
        if (iSectionService.isSectionNameExists(sectionRequest.getName())) {
            return new ResponseEntity<>(new MessageResponse("Tên dạng phần đã tồn tại"), HttpStatus.BAD_REQUEST);
        }

        try {
            Section updatedSection = iSectionService.updateSection(sectionRequest);

            if (updatedSection != null) {
                return ResponseEntity.ok(new MessageResponse("Cập nhật dạng phần thành công!"));
            } else {
               return new ResponseEntity<>(new MessageResponse("Cập nhật dạn phần thất bại!"), HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Lỗi khi cập nhật dạng phần: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/section/delete/{id}")
    public ResponseEntity<MessageResponse> deleteSection(@PathVariable Integer id) {
       try {
           boolean result = iSectionService.deleteSection(id);

           if(result) {
               return ResponseEntity.ok(new MessageResponse("Xóa dạng phần thành công!"));
           } else {
               return new ResponseEntity<>(new MessageResponse("Xoá dạng phần thất bại!"), HttpStatus.BAD_REQUEST);
           }
       } catch (IOException e) {
           return new ResponseEntity<>(new MessageResponse("Lỗi khi xoá dạng phần: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @PutMapping("/admin/section/update-status/{id}")
    public ResponseEntity<MessageResponse> updateSectionStatus(@PathVariable Integer id, @RequestBody Integer newStatus) {
        Section updateSection = iSectionService.updateSectionStatus(id, newStatus);

        if(updateSection != null) {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái cho dạng phần thành công!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Cập nhật trạng thái cho dạng phần thất bại!"), HttpStatus.BAD_REQUEST);
        }
    }



}
