package com.backend.spring.controller;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class FreeMaterialController {

    @Autowired
    private IFreeMaterialService iFreeMaterialService;

//  Admin
    @GetMapping("/admin/free-material/get-all")
    public ResponseData<List<FreeMaterialResponse>> getAllFreeMaterials() {
        List<FreeMaterialResponse> freeMaterialList = iFreeMaterialService.getAllFreeMaterials().stream().map(
                FreeMaterialMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, freeMaterialList);
    }

//  Người dùng
    @GetMapping("/public/free-material/get-all/enable")
    public ResponseData<List<FreeMaterialResponse>> getAllEnableFreeMaterials() {
        List<FreeMaterialResponse> freeMaterialList = iFreeMaterialService.getAllFreeMaterials().stream().map(
                FreeMaterialMapper::mapFromEntityToResponse
        ).collect(Collectors.toList());

        // Lọc danh sách chỉ giữ lại các FreeMaterial có freeMaterialStatus là 1 (enable)
        List<FreeMaterialResponse> filteredFreeMaterials = freeMaterialList.stream()
                .filter(freeMaterial -> freeMaterial.getMaterialStatus().equals(EStatus.ENABLE.getValue()))
                .collect(Collectors.toList());

        if (!filteredFreeMaterials.isEmpty()) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, filteredFreeMaterials);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.FreeMaterial.DATA_NOT_FOUND);
        }
    }

    @GetMapping("/admin/free-material/get-by-id/{id}")
    public ResponseData<FreeMaterialResponse> getFreeMaterialById(@PathVariable("id") @Min(1) Integer id) {
        FreeMaterialResponse freeMaterial = FreeMaterialMapper.mapFromEntityToResponse(iFreeMaterialService.getFreeMaterialById(id));

        if (freeMaterial != null) {
            return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, freeMaterial);
        } else {
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), MessageConstant.FreeMaterial.DATA_NOT_FOUND);
        }
    }

    @PostMapping("/admin/free-material/create")
    public ResponseData<MessageResponse> createFreeMaterial(@ModelAttribute @Valid FreeMaterialRequest freeMaterialRequest) {
        try {
            FreeMaterial createdFreeMaterial = iFreeMaterialService.createFreeMaterial(freeMaterialRequest);

            if(createdFreeMaterial != null) {
                return new ResponseData<>(HttpStatus.CREATED.value(), MessageConstant.FreeMaterial.ADD_NEW_SUCCESS);
            } else {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.FreeMaterial.ADD_NEW_FAILED);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PutMapping("/admin/free-material/update")
    public ResponseData<?> updateFreeMaterial(@ModelAttribute @Valid FreeMaterialRequest freeMaterialRequest) {
        try {
            FreeMaterial updatedFreeMaterial = iFreeMaterialService.updateFreeMaterial(freeMaterialRequest);

            if (updatedFreeMaterial != null) {
                return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.FreeMaterial.UPDATE_SUCCESS);
            } else {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.FreeMaterial.UPDATE_FAILED);
            }
        } catch (IOException | java.io.IOException e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @DeleteMapping("/admin/free-material/delete/{id}")
    public ResponseData<MessageResponse> deleteFreeMaterial(@PathVariable("id") @Min(1) Integer id) {
       try {
           boolean result = iFreeMaterialService.deleteFreeMaterial(id);

           if(result) {
               return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.FreeMaterial.DELETE_SUCCESS);
           } else {
               return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.FreeMaterial.DELETE_FAILED);
           }
       } catch (IOException | java.io.IOException e) {
           return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
       }
    }

    @PutMapping("/admin/free-material/update-status/{id}")
    public ResponseData<MessageResponse> updateFreeMaterialStatus(@PathVariable("id") @Min(1) Integer id, @RequestBody Integer newStatus) {
        boolean result = iFreeMaterialService.updateFreeMaterialStatus(id, newStatus);

        if(result) {
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), MessageConstant.FreeMaterial.UPDATE_STATUS_SUCCESS);
        }else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), MessageConstant.FreeMaterial.UPDATE_STATUS_FAILED);
        }
    }

    @GetMapping("/admin/free-material/total")
    public ResponseData<Long> countTotalFreeMaterials() {
        long totalFreeMaterials = iFreeMaterialService.countTotalFreeMaterials();

        return new ResponseData<>(HttpStatus.OK.value(), MessageConstant.FreeMaterial.GET_DATA_SUCCESS, totalFreeMaterials);
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
