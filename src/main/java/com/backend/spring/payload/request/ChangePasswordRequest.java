package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "Mật khẩu cũ không được trống!")
    @Size(min = 6, message = "Mật khẩu phải có tối thiểu 6 ký tự")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được trống!")
    @Size(min = 6, message = "Mật khẩu phải có tối thiểu 6 ký tự")
    private String newPassword;
}
