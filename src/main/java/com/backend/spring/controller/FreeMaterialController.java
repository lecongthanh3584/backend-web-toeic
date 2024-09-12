package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.mapper.FreeMaterialMapper;
import com.backend.spring.entity.FreeMaterial;
import com.backend.spring.payload.request.FreeMaterialRequest;
import com.backend.spring.payload.response.FreeMaterialResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.service.FreeMaterial.IFreeMaterialService;
import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
@RequestMapping("/api")
@Validated
public class FreeMaterialController {

    @Autowired
    private IFreeMaterialService iFreeMaterialService;

//  Admin
    @GetMapping("/admin/free-material/get-all")
    public ResponseEntity<?> getAllFreeMaterials() {
        List<FreeMaterialResponse> freeMaterialList = iFreeMaterialService.getAllFreeMaterials().stream().map(
                FreeMaterialMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS,
                freeMaterialList), HttpStatus.OK);
    }

//  Người dùng
    @GetMapping("/public/free-material/get-all/enable")
    public ResponseEntity<?> getAllEnableFreeMaterials() {
        List<FreeMaterialResponse> freeMaterialList = iFreeMaterialService.getAllFreeMaterials().stream().map(
                FreeMaterialMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các FreeMaterial có freeMaterialStatus là 1 (enable)
        List<FreeMaterialResponse> filteredFreeMaterials = freeMaterialList.stream()
                .filter(freeMaterial -> freeMaterial.getMaterialStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredFreeMaterials.isEmpty()) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, filteredFreeMaterials),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_NOT_FOUND.getValue(), MessageConstant.FreeMaterial.DATA_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
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
