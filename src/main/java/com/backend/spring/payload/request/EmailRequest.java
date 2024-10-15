package com.backend.spring.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EmailRequest {

    @NotBlank(message = "Email không được bỏ trống.")
    @Email(message = "Email không đúng định dạng.")
    private String email;
}
