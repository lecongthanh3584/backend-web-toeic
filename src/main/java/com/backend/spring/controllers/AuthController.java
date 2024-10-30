package com.backend.spring.controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.entities.*;
import com.backend.spring.exception.AlreadyExistsException;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.payload.request.*;
import com.backend.spring.payload.response.RefreshTokenResponse;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.security.service.CustomUserDetails;
import com.backend.spring.services.Auth.IAuthService;
import com.backend.spring.services.RefreshToken.IRefreshTokenService;
import com.backend.spring.utils.UserUtil;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.spring.payload.response.SigninResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.security.jwt.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final IAuthService iAuthService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final IRefreshTokenService iRefreshTokenService;

    @Value("${spring.domain.front-end}")
    private String domainFrontEnd;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User userLogin = userDetails.getUser();

            // Kiểm tra nếu tài khoản chưa được kích hoạt
            if (userLogin.getIsActive().equals(EStatus.INACTIVATE.getValue())) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.ACCOUNT_INACTIVATED.getValue(), MessageConstant.Auth.ACCOUNT_INACTIVATED),
                        HttpStatus.FORBIDDEN);
            }

            // Kiểm tra nếu tài khoản có bị khoá hay không
            if (userLogin.getStatus().equals(EStatus.DISABLE.getValue())) {
                return new ResponseEntity<>(new ResponseData<>(EStatusCode.ACCOUNT_LOCKED.getValue(), MessageConstant.Auth.ACCOUNT_LOCKED),
                        HttpStatus.FORBIDDEN);
            }

            // Tạo Access Token và Refresh Token
            String accessToken = jwtUtil.generateAccessToken(userLogin);
            String refreshToken = iRefreshTokenService.generateRefreshToken(userLogin.getUserId());

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Tính toán thời gian hết hạn của Access Token và Refresh Token
            long accessTokenExpirationTime = jwtUtil.getAccessTokenDurationMs();

            SigninResponse signinResponse = SigninResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                    .accessTokenExpirationTime(accessTokenExpirationTime).roles(roles).build();

            // Trả về response với thông tin thời gian hết hạn dưới dạng Date
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNIN_SUCCESS.getValue(), MessageConstant.Auth.SIGNIN_SUCCESS, signinResponse),
                    HttpStatus.OK);


        } catch (BadCredentialsException e) {
            // Xử lý khi tên đăng nhập hoặc mật khẩu không đúng
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNIN_FAILED.getValue(), MessageConstant.Auth.SIGNIN_FAILED),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest) throws AlreadyExistsException, NotFoundException {
        boolean resultRegis = iAuthService.registerUser(signupRequest);

        if(resultRegis) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNUP_SUCCESS.getValue(), MessageConstant.Auth.SIGNUP_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNUP_FAILED.getValue(), MessageConstant.Auth.SIGNUP_FAILED),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestBody @Valid ActivateAccountRequest request) {
        Integer status = iAuthService.verifyAccount(request);

        if(status.equals(EStatusCode.ACCOUNT_ACTIVATE_SUCCESS.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(status, MessageConstant.Auth.ACTIVATE_ACCOUNT_SUCCESS),
                    HttpStatus.OK);

        } else if (status.equals(EStatusCode.ACCOUNT_ACTIVATED.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(status, MessageConstant.Auth.ACCOUNT_ACTIVATED),
                    HttpStatus.OK);

        } else if (status.equals(EStatusCode.ACCOUNT_ACTIVATE_FAILED.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(status, MessageConstant.Auth.ACTIVATE_ACCOUNT_FAILURE),
                    HttpStatus.BAD_REQUEST);

        } else if (status.equals(EStatusCode.EMAIL_ACTIVATE_EXPIRED.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(status, MessageConstant.Auth.ACTIVATE_MAIL_EXPIRED),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/send-new-activation-email")
    public ResponseEntity<?> sendNewVefificationEmail(@RequestBody @Valid EmailRequest emailRequest) {
        Integer result = iAuthService.sendNewVerificationEmail(emailRequest);

        if (result.equals(EStatusCode.SEND_MAIL_SUCCESS.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.SEND_NEW_VERIFICATION_MAIL_SUCCESS),
                    HttpStatus.OK);
        } else if(result.equals(EStatusCode.EMAIL_NOT_EXISTED.getValue())){
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.EMAIL_NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        } else if(result.equals(EStatusCode.ACCOUNT_ACTIVATED.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.ACCOUNT_ACTIVATED),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/get-new-access-token")
    public ResponseEntity<?> getNewAccessToken(@Valid @RequestBody RefreshTokenRequest request) throws Exception {
        RefreshTokenResponse refreshTokenResponse = iRefreshTokenService.getNewAccessToken(request);

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.GET_DATA_SUCCESS.getValue(), MessageConstant.Auth.GET_NEW_ACCESS_TOKEN_SUCCESS, refreshTokenResponse),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        User userLogin = UserUtil.getDataUserLogin();
        boolean result = iAuthService.signOut(userLogin);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNUP_SUCCESS.getValue(), MessageConstant.Auth.SIGNOUT_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNOUT_FAILURE.getValue(), MessageConstant.Auth.SIGNOUT_FAILURE),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-email-exists")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        try {
            boolean emailExists = userRepository.existsByEmail(email);
            return ResponseEntity.ok(emailExists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to check email existence"));
        }
    }

    @PostMapping("/forgot-password-email")
    public ResponseEntity<?> forgotPasswordMail(@RequestBody @Valid EmailRequest emailRequest) {
        boolean result = iAuthService.forgotPasswordMail(emailRequest);

        if(result) {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SEND_MAIL_SUCCESS.getValue(), MessageConstant.Auth.SEND_RESET_MAIL_SUCCESS),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(EStatusCode.SEND_MAIL_FAILURE.getValue(), MessageConstant.Auth.EMAIL_NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> handleResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        Integer result = iAuthService.handleResetPassword(request);

        if(result.equals(EStatusCode.EMAIL_NOT_EXISTED.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.EMAIL_NOT_EXISTED),
                    HttpStatus.BAD_REQUEST);
        } else if (result.equals(EStatusCode.TOKEN_AUTH_INVALID.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.TOKEN_AUTH_NOT_VALID),
                    HttpStatus.BAD_REQUEST);

        } else if (result.equals(EStatusCode.TOKEN_AUTH_EXPIRED.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.TOKEN_AUTH_EXPIRED),
                    HttpStatus.BAD_REQUEST);

        } else if (result.equals(EStatusCode.DATA_INVALID.getValue())) {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.DATA_INVALID),
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ResponseData<>(result, MessageConstant.Auth.RESET_PASSWORD_SUCCESS),
                    HttpStatus.OK);
        }
    }

    @PostMapping("/oauth2/login-google")
    public ResponseEntity<?> handleOauth2LoginGoogle(@RequestBody @Valid OAuth2GoogleRequest request) throws GeneralSecurityException, IOException {
        SigninResponse response = iAuthService.handleLoginOAuth2Google(request.getAccessTokenOAuth2());

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNIN_SUCCESS.getValue(), MessageConstant.Auth.SIGNIN_SUCCESS, response), HttpStatus.OK);
    }

    @PostMapping("/oauth2/login-facebook")
    public ResponseEntity<?> handleOauth2LoginFacebook(@RequestBody @Valid OAuth2FbRequest request) {
        SigninResponse response = iAuthService.handleLoginOAuth2Facebook(request);

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.SIGNIN_SUCCESS.getValue(), MessageConstant.Auth.SIGNIN_SUCCESS, response), HttpStatus.OK);
    }
}
