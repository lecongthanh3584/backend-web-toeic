package com.backend.spring.payload.request;

import com.backend.spring.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VocabularyRequest {
    private Integer vocabularyId;

    @NotBlank(message = "Từ vựng không được bỏ trống!")
    private String word;

    @NotBlank(message = "Phiên âm IPA không được bỏ trống!")
    private String ipa;

    @NotBlank(message = "Ý nghĩa của từ vựng không được bỏ trống!")
    private String meaning;

    @NotBlank(message = "Câu ví dụ không được bỏ trống!")
    private String exampleSentence;

    private MultipartFile image;

    @NotNull(message = "Id của topic không được bỏ trống!")
    private Integer topicId;

    private Integer vocabularyStatus = EStatus.ENABLE.getValue();
}
