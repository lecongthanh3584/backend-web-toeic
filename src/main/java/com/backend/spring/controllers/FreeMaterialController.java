package com.backend.spring.controllers;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.ExamMapper;
import com.backend.spring.mapper.FreeMaterialMapper;
import com.backend.spring.entities.FreeMaterial;
import com.backend.spring.payload.request.FreeMaterialRequest;
import com.backend.spring.payload.response.ExamResponse;
import com.backend.spring.payload.response.FreeMaterialResponse;
import com.backend.spring.payload.response.main.PaginationData;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.services.FreeMaterial.IFreeMaterialService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class FreeMaterialController {

    private final IFreeMaterialService iFreeMaterialService;

//  Admin
    @GetMapping("/admin/free-material/get-all")
    public ResponseEntity<?> getAllFreeMaterials(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "created_at:desc") String... sortBys
    ) {
        Page<FreeMaterial> freeMaterialPage = iFreeMaterialService.getAllFreeMaterials(page, keyword, sortBys);

        PaginationData paginationData = PaginationData.builder().totalPage(freeMaterialPage.getTotalPages()).totalElement(freeMaterialPage.getTotalElements())
                .pageNumber(freeMaterialPage.getPageable().getPageNumber()).pageSize(freeMaterialPage.getPageable().getPageSize()).build();

        List<FreeMaterialResponse> freeMaterialResponses = freeMaterialPage.getContent().stream().map(
                FreeMaterialMapper::mapFromEntityToResponse
        ).toList();

        if(!freeMaterialResponses.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, paginationData,
                    freeMaterialResponses), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.FreeMaterial.DATA_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/admin/free-material/get-by-id/{id}")
    public ResponseEntity<?> getFreeMaterialById(@PathVariable("id") @Min(1) Integer id) {
        FreeMaterialResponse freeMaterial = FreeMaterialMapper.mapFromEntityToResponse(iFreeMaterialService.getFreeMaterialById(id));

        if (freeMaterial != null) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, freeMaterial),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.FreeMaterial.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/free-material/create")
    public ResponseEntity<?> createFreeMaterial(@ModelAttribute @Valid FreeMaterialRequest freeMaterialRequest) {
        try {
            FreeMaterial createdFreeMaterial = iFreeMaterialService.createFreeMaterial(freeMaterialRequest);

            if(createdFreeMaterial != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_SUCCESS.getValue(), MessageConstant.FreeMaterial.ADD_NEW_SUCCESS),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), MessageConstant.FreeMaterial.ADD_NEW_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.CREATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/free-material/update")
    public ResponseEntity<?> updateFreeMaterial(@ModelAttribute @Valid FreeMaterialRequest freeMaterialRequest) {
        try {
            FreeMaterial updatedFreeMaterial = iFreeMaterialService.updateFreeMaterial(freeMaterialRequest);

            if (updatedFreeMaterial != null) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.FreeMaterial.UPDATE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.FreeMaterial.UPDATE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/free-material/delete/{id}")
    public ResponseEntity<?> deleteFreeMaterial(@PathVariable("id") @Min(1) Integer id) {
        try {
            boolean result = iFreeMaterialService.deleteFreeMaterial(id);

            if(result) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_SUCCESS.getValue(), MessageConstant.FreeMaterial.DELETE_SUCCESS),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), MessageConstant.FreeMaterial.DELETE_FAILED),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DELETE_FAILED.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/free-material/update-status/{id}")
    public ResponseEntity<?> updateFreeMaterialStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
        boolean result = iFreeMaterialService.updateFreeMaterialStatus(id, newStatus);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_SUCCESS.getValue(), MessageConstant.FreeMaterial.UPDATE_STATUS_SUCCESS) ,
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.UPDATE_FAILED.getValue(), MessageConstant.FreeMaterial.UPDATE_STATUS_FAILED) ,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/free-material/total")
    public ResponseEntity<?> countTotalFreeMaterials() {
        long totalFreeMaterials = iFreeMaterialService.countTotalFreeMaterials();

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, totalFreeMaterials),
                HttpStatus.OK);
    }


    //  Người dùng
    @GetMapping("/public/free-material/get-all/enable")
    public ResponseEntity<?> getAllEnableFreeMaterials(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {

        Page<FreeMaterial> freeMaterialPage = iFreeMaterialService.getAllFreeMaterialsEnable(page, keyword);

        PaginationData paginationData = PaginationData.builder().totalPage(freeMaterialPage.getTotalPages()).totalElement(freeMaterialPage.getTotalElements())
                .pageNumber(freeMaterialPage.getPageable().getPageNumber()).pageSize(freeMaterialPage.getPageable().getPageSize()).build();

        List<FreeMaterialResponse> freeMaterialResponses = freeMaterialPage.getContent().stream().map(
                FreeMaterialMapper::mapFromEntityToResponse
        ).toList();

        if(!freeMaterialResponses.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, paginationData,
                    freeMaterialResponses), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.FreeMaterial.DATA_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/public/free-material/download/{filename}")
    public ResponseEntity<Resource> downloadFreeMaterial(@PathVariable String filename) {
        // Đường dẫn tới thư mục chứa tệp PDF
        String pdfFolderPath = "static/pdfs/";

        // Tạo một ClassPathResource từ đường dẫn của tệp PDF
        Resource resource = new ClassPathResource(pdfFolderPath + filename);

        // Kiểm tra xem tệp có tồn tại không
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // Chuyển đổi tên tệp thành dạng không Unicode (UTF-8)
        String sanitizedFilename = sanitizeFilename(filename);

        // Thiết lập các tiêu đề và loại dữ liệu
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + sanitizedFilename);
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    private String sanitizeFilename(String filename) {
        // Chuyển đổi ký tự Unicode thành dạng không Unicode (UTF-8)
        return new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }
}
