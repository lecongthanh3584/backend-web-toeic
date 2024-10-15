package com.backend.spring.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateAccountRequest {

    @NotBlank(message = "Mã Token không được bỏ trống.")
    private String token;
}
