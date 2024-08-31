package com.backend.spring.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.enums.EUserStatus;
import com.backend.spring.entity.*;
import com.backend.spring.payload.request.*;
import com.backend.spring.payload.response.main.ResponseData;
import com.backend.spring.security.service.CustomUserDetails;
import com.backend.spring.service.Email.EmailService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.spring.payload.response.SigninResponse;
import com.backend.spring.payload.response.MessageResponse;
import com.backend.spring.repository.RoleRepository;
import com.backend.spring.repository.UserRepository;
import com.backend.spring.security.jwt.JwtUtil;
import com.backend.spring.security.service.RefreshTokenService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User userLogin = userDetails.getUser();

            // Kiểm tra nếu tài khoản chưa được kích hoạt
            if (userLogin.getIsActive().equals(EUserStatus.INACTIVATE.getValue())) {
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
            String refreshToken = refreshTokenService.generateRefreshToken(userLogin.getUserId()).getToken();

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Tính toán thời gian hết hạn của Access Token và Refresh Token
            long accessTokenExpirationTime = jwtUtil.getAccessTokenDurationMs();
            long refreshTokenExpirationTime = refreshTokenService.getRefreshTokenDurationMs();

            SigninResponse signinResponse = SigninResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                    .accessTokenExpirationTime(accessTokenExpirationTime).refreshTokenExpirationTime(refreshTokenExpirationTime)
                    .roles(roles).build();

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã được sử dụng!"));
        }
        if (userRepository.existsByPhoneNumber(signUpDto.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse("SĐT đã được sử dụng!"));
        }
        // Create new user's account
        User user = new User(
                signUpDto.getFullName(),
                signUpDto.getUsername(),
                signUpDto.getEmail(),
                encoder.encode(signUpDto.getPassword()),
                signUpDto.getAddress(),
                signUpDto.getPhoneNumber(),
                signUpDto.getGender()
        );
        // Tạo mã xác thực
        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);

        Set<String> strRoles = signUpDto.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.LEARNER)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(ERole.LEARNER)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        // Gửi email xác thực
        String subject = "Xác thực tài khoản";
        // Đọc nội dung của file template
        String templateContent = loadVerificationEmailTemplate(verificationCode);
        // Gửi email sử dụng template
        emailService.sendEmail(signUpDto.getEmail(), subject, templateContent);

        return ResponseEntity.ok(new MessageResponse("Đăng kí người dùng dành công"));
    }
    private String loadVerificationEmailTemplate(String verificationCode) {
        try {
            Resource resource = new ClassPathResource("templates/verification.html");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            // Link xác thực
            return content.toString().replace("${url}", "http://localhost:9004/api/auth/activate-account?verificationCode=" + verificationCode);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam("verificationCode") String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy người dùng với mã là: " + verificationCode));
        }
        if (user.getIsActive() == 1) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản này đã được kích hoạt!!!"));
        }
        user.setIsActive(1);
        userRepository.save(user);
        // Chuyển hướng tới trang frontend và truyền mã xác thực trong URL
        String frontendRedirectUrl = "http://localhost:3002/verification?verificationCode=" + verificationCode;
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", frontendRedirectUrl)
                .body(new MessageResponse("Tài khoản đã được kích hoạt thành công. Redirecting..."));
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshDto request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(user.getUsername(), user.getUserId());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token không có trong CSDL!"));
//    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userLogin = userDetails.getUser();
        Integer userId = userLogin.getUserId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
    }

//  Gửi email lại
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmailBack(@RequestBody EmailDto emailDto) {
        try {
            System.out.println(emailDto.getTo());
            // Tìm kiếm người dùng bằng email
            User user = userRepository.findByEmail(emailDto.getTo());
            if (user == null) {
                System.err.println("User not found with email: " + emailDto.getTo());
                return ResponseEntity.badRequest().body("User not found with the provided email.");
            }
            // Tạo mã xác thực mới
            String newVerificationCode = UUID.randomUUID().toString();
            user.setVerificationCode(newVerificationCode);
            userRepository.save(user);

            // Gửi email xác thực mới
            String subject = "Xác thực tài khoản";
            String body = "Nhấn vào liên kết sau để xác thực tài khoản:\n";
            body += "<a href='http://localhost:9004/api/auth/activate-account?verificationCode=" + newVerificationCode + "'>Xác thực tài khoản</a>";

            emailService.sendEmail(emailDto.getTo(), subject, body);

            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
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





}
