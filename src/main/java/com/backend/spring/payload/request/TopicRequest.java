package com.backend.spring.payload.request;
import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TopicRequest {
    private Integer topicId;

    @NotBlank(message = "Tên của topic không được bỏ trống!")
    private String topicName;

    private MultipartFile image;

    private Integer topicStatus = EStatus.ENABLE.getValue();
}


