package com.backend.spring.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigninResponse {
    private String accessToken;

    private String refreshToken;

    private List<String> roles;

    private Long accessTokenExpirationTime; // Thêm thông tin thời gian hết hạn của Access Token

    private Long refreshTokenExpirationTime; // Thêm thông tin thời gian hết hạn của Refresh Token

//    private Integer id;
//    private String username;
//    private String email;
//    private String address;
//    private String phoneNumber;
//    private Integer gender;
//    private Integer status;
//    private Integer isActive;
//    private String verificationCode;
//    private String name;
}
