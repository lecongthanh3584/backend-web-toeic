package com.backend.spring.payload.request;
import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FreeMaterialRequest {
    private Integer materialId;

    @NotBlank(message = "Tiêu đề tài liệu không được trống!")
    private String title;

    private String description;

    @NotNull
    private MultipartFile filePdf;

    private Integer materialStatus = EStatus.ENABLE.getValue();
}
