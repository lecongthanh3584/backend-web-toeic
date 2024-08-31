package com.backend.spring.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "Username không được bỏ trống!")
    @Size(min = 3, max = 30, message = "Username tối thiểu 3 ký tự và tối đa 30 ký tự!")
    private String username;

    @NotBlank(message = "Email không được bỏ trống!")
    @Size(max = 50, message = "Email tối đa 50 ký tự!")
    @Email(message = "Email không hợp lệ!")
    private String email;

    @NotBlank(message = "Mật khẩu không được bỏ trống!")
    @Size(min = 6, max = 40, message = "Mật khẩu tối thiểu 6 ký tự và tối đa 40 ký tự!")
    private String password;

    @NotBlank(message = "Tên người dùng không được bỏ trống!")
    private String fullName;

    private String address;

    private String phoneNumber;

    private Integer gender;

    // Danh sách không trùng lặp
    private Set<String> role;

}
