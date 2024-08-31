package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotNull(message = "Id người dùng phải có giá trị!")
    private Integer userId;

    @NotBlank(message = "Mật khẩu cũ không được trống!")
    private String oldPassWord;

    @NotBlank(message = "Mật khẩu mới không được trống!")
    private String newPassWord;
}
