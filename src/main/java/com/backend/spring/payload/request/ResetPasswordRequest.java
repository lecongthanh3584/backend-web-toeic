package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank(message = "Email không được trống.")
    private String email;

    @NotBlank(message = "Token không được trống.")
    private String token;

    @NotBlank(message = "Mật khẩu mới không được trống.")
    @Size(min = 6, message = "Mật khẩu mới tối thiểu 6 ký tự.")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu mới không được trống.")
    @Size(min = 6, message = "Xác nhận mật khẩu mới tối thiểu 6 ký tự.")
    private String confirmNewPassword;
}
