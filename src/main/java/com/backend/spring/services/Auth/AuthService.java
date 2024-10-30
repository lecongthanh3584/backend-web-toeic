package com.backend.spring.services.Auth;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.entities.AuthToken;
import com.backend.spring.entities.Role;
import com.backend.spring.entities.User;
import com.backend.spring.enums.*;
import com.backend.spring.exception.AlreadyExistsException;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.exception.OAuth2AuthenticationProcessingException;
import com.backend.spring.payload.request.*;
import com.backend.spring.payload.response.SigninResponse;
import com.backend.spring.repositories.AuthTokenRepository;
import com.backend.spring.repositories.RoleRepository;
import com.backend.spring.repositories.UserRepository;
import com.backend.spring.security.jwt.JwtUtil;
import com.backend.spring.services.Email.EmailService;
import com.backend.spring.services.Redis.IRedisService;
import com.backend.spring.services.RefreshToken.IRefreshTokenService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthTokenRepository authTokenRepository;

    private final EmailService emailService;

    private final IRedisService iRedisService;

    private final JwtUtil jwtUtil;

    private final IRefreshTokenService iRefreshTokenService;

    private final GoogleIdTokenVerifier verifier;

    @Value("${spring.domain.front-end}")
    private String domainFrontEnd;

    @Override
    public boolean registerUser(SignupRequest signupRequest) throws AlreadyExistsException, NotFoundException {
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AlreadyExistsException(MessageConstant.Auth.EMAIL_IS_USED);
        } else if(userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new AlreadyExistsException(MessageConstant.Auth.USERNAME_IS_USED);
        }

        //Kiểm tra role
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.LEARNER)
                    .orElseThrow(() -> new NotFoundException(MessageConstant.Auth.ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                if (role.equals(ERole.ADMIN.name())) {
                    Role adminRole = roleRepository.findByRoleName(ERole.ADMIN)
                            .orElseThrow(() -> new NotFoundException(MessageConstant.Auth.ROLE_NOT_FOUND));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByRoleName(ERole.LEARNER)
                            .orElseThrow(() -> new NotFoundException(MessageConstant.Auth.ROLE_NOT_FOUND));
                    roles.add(userRole);
                }
            }
        }

        //Tạo mới đối tượng user
        User userRegister = User.builder()
                .fullName(signupRequest.getFullName()).email(signupRequest.getEmail())
                .username(signupRequest.getUsername()).password(passwordEncoder.encode(signupRequest.getPassword()))
                .address(signupRequest.getAddress()).phoneNumber(signupRequest.getPhoneNumber())
                .isActive(EStatus.INACTIVATE.getValue()).status(EStatus.ENABLE.getValue()).provider(EProvider.LOCAL.getValue())
                .roles(roles).build();

        userRepository.save(userRegister);

        String token = UUID.randomUUID().toString();

        //Tạo mới đối tượng verify
        AuthToken authToken = AuthToken.builder().token(token).isUsed(EStatus.INACTIVATE.getValue())
                .user(userRegister).type(EAuthToken.VERIFY_ACCOUNT.getValue()).expiredAt(LocalDateTime.now().plusHours(24)).build();

        authTokenRepository.save(authToken);

        String templateEmailContent = loadVerificationAccountTemplate(token, signupRequest.getFullName(), domainFrontEnd);

        // Gửi email sử dụng template
        emailService.sendEmail(signupRequest.getEmail(), MessageConstant.Auth.ACTIVATE_ACCOUNT, templateEmailContent);
        return true;
    }

    @Override
    public boolean signOut(User user) {
        String key = MessageConstant.Redis.REFRESH_TOKEN + ":" + user.getUserId();
        iRedisService.deleteDataCache(key);

        return true;
    }

    @Override
    public Integer verifyAccount(ActivateAccountRequest request) {
        Optional<AuthToken> authTokenOptional = authTokenRepository.findByToken(request.getToken());
        if(authTokenOptional.isEmpty()) {
            return EStatusCode.ACCOUNT_ACTIVATE_FAILED.getValue();
        }

        AuthToken authToken = authTokenOptional.get();
        if(authToken.getIsUsed().equals(EStatus.ACTIVATE.getValue())
                && authToken.getUser().getIsActive().equals(EStatus.ACTIVATE.getValue())) {
            return EStatusCode.ACCOUNT_ACTIVATED.getValue();
        } else if(authToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            return EStatusCode.EMAIL_ACTIVATE_EXPIRED.getValue();
        }

        authToken.getUser().setIsActive(EStatus.ACTIVATE.getValue());
        authToken.setIsUsed(EStatus.ACTIVATE.getValue());

        userRepository.save(authToken.getUser());
        authTokenRepository.save(authToken);

        return EStatusCode.ACCOUNT_ACTIVATE_SUCCESS.getValue();
    }

    @Override
    public Integer sendNewVerificationEmail(EmailRequest emailRequest) {
        Optional<User> userOptional = userRepository.findByEmail(emailRequest.getEmail());
        if(userOptional.isEmpty()) {
            return EStatusCode.EMAIL_NOT_EXISTED.getValue();
        } else if(userOptional.get().getIsActive().equals(EStatus.ACTIVATE.getValue())) {
            return EStatusCode.ACCOUNT_ACTIVATED.getValue();
        }

        String token = UUID.randomUUID().toString();
        AuthToken authToken = AuthToken.builder().token(token).user(userOptional.get()).isUsed(EStatus.INACTIVATE.getValue())
                .expiredAt(LocalDateTime.now().plusDays(1)).type(EAuthToken.VERIFY_ACCOUNT.getValue()).build();

        authTokenRepository.save(authToken);
        String templateContent = loadVerificationAccountTemplate(token, userOptional.get().getFullName(), domainFrontEnd);

        emailService.sendEmail(emailRequest.getEmail(), MessageConstant.Auth.ACTIVATE_ACCOUNT, templateContent);

        return EStatusCode.SEND_MAIL_SUCCESS.getValue();
    }

    @Override
    public boolean forgotPasswordMail(EmailRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if(userOptional.isEmpty()) {
            return false;
        }

        String token = UUID.randomUUID().toString();
        AuthToken authToken = AuthToken.builder().token(token).isUsed(EStatus.INACTIVATE.getValue())
                .type(EAuthToken.RESET_PASSWORD.getValue()).user(userOptional.get())
                .expiredAt(LocalDateTime.now().plusDays(1)).build();

        String templateContentEmail = loadResetPasswordEmailTemplate(token, userOptional.get().getFullName(), userOptional.get().getEmail(), domainFrontEnd);
        emailService.sendEmail(request.getEmail(), MessageConstant.Auth.RESET_PASSWORD, templateContentEmail);

        authTokenRepository.save(authToken);
        return true;

    }

    @Override
    public Integer handleResetPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if(userOptional.isEmpty()) {
            return EStatusCode.EMAIL_NOT_EXISTED.getValue();
        }

        Optional<AuthToken> authTokenOptional = authTokenRepository.findByTokenAndUser(request.getToken(), userOptional.get());

        if(authTokenOptional.isEmpty()) {
            return EStatusCode.TOKEN_AUTH_INVALID.getValue();
        } else if (authTokenOptional.get().getExpiredAt().isBefore(LocalDateTime.now())
                || authTokenOptional.get().getIsUsed().equals(EStatus.ACTIVATE.getValue())) {
            return EStatusCode.TOKEN_AUTH_EXPIRED.getValue();
        }

        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return EStatusCode.DATA_INVALID.getValue();
        }

        userOptional.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
        authTokenOptional.get().setIsUsed(EStatus.ACTIVATE.getValue());

        userRepository.save(userOptional.get());
        authTokenRepository.save(authTokenOptional.get());

        return EStatusCode.RESET_PASSWORD_SUCCESS.getValue();
    }

    private String loadVerificationAccountTemplate(String token, String fullName, String domainFrontEnd) {
        String templateUrl = "templates/activateAccount.html";
        String emailTemplate = loadEmailTemplate(templateUrl);

        return emailTemplate
                .replace("${url}", domainFrontEnd + "/verify?token=" + token)
                .replace("${receiver}", fullName);
    }

    private String loadResetPasswordEmailTemplate(String token, String fullName, String email, String domainFrontEnd) {
        String templateUrl = "templates/resetPasswordEmail.html";
        String emailTemplate = loadEmailTemplate(templateUrl);
        return emailTemplate
                .replace("${url}", domainFrontEnd + "/reset-password?token=" + token + "&email=" + email)
                .replace("${receiverName}", fullName);
    }

    private String loadEmailTemplate(String templateName) {
        try {
            Resource resource = new ClassPathResource(templateName);
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;

            // Đọc từng dòng trong file
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            // Đóng reader
            reader.close();

            // Link xác thực
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public SigninResponse handleLoginOAuth2Google(String tokenOAuth2) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken = verifier.verify(tokenOAuth2);
        if(googleIdToken == null) {
            return null;
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        Optional<User> userOptional = userRepository.findByEmail(payload.getEmail());

        User userLogin = null; //Lấy thông tin người dùng đăng nhập

        if(userOptional.isPresent()) {

            if(!userOptional.get().getProvider().equals(EProvider.GOOGLE.getValue())) {
                throw new OAuth2AuthenticationProcessingException(EStatusCode.OAUTH2_EMAIL_EXISTED.getValue(), MessageConstant.Auth.EMAIL_IS_USED);
            }

            userLogin = updateExistUser(userOptional.get(), payload);
        } else {
            Role roleLearner = roleRepository.findByRoleName(ERole.LEARNER).orElseThrow(() -> new RuntimeException(MessageConstant.Auth.ROLE_NOT_FOUND));

            userLogin = registerNewUser(payload, roleLearner);
        }

        return generateAccessTokenAndRefreshToken(userLogin);
    }

    @Override
    public SigninResponse handleLoginOAuth2Facebook(OAuth2FbRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        User userLogin = null; //Lấy thông tin người dùng đăng nhập

        if(userOptional.isPresent()) {

            if(!userOptional.get().getProvider().equals(EProvider.FACEBOOK.getValue())) {
                throw new OAuth2AuthenticationProcessingException(EStatusCode.OAUTH2_EMAIL_EXISTED.getValue(), MessageConstant.Auth.EMAIL_IS_USED);
            }

            userLogin = updateExistUser(userOptional.get(), request);
        } else {
            Role roleLearner = roleRepository.findByRoleName(ERole.LEARNER).orElseThrow(() -> new RuntimeException(MessageConstant.Auth.ROLE_NOT_FOUND));

            userLogin = registerNewUser(request, roleLearner);
        }

        return generateAccessTokenAndRefreshToken(userLogin);
    }

    //Đăng ký tài khoản mới khi đăng nhập bằng google
    private User registerNewUser(GoogleIdToken.Payload payload, Role role) {
        User newUser = User.builder().fullName((String) payload.get("name")).image((String) payload.get("picture")).isActive(EStatus.ACTIVATE.getValue())
                .email(payload.getEmail()).status(EStatus.ENABLE.getValue()).roles(Set.of(role)).provider(EProvider.GOOGLE.getValue())
                .build();

        return userRepository.save(newUser);
    }

    //Cập nhật tài khoản khi tài khoản đã tồn tại
    private User updateExistUser(User existUser, GoogleIdToken.Payload payload) {
        existUser.setFullName((String) payload.get("name"));
        existUser.setImage((String) payload.get("picture"));

        return userRepository.save(existUser);
    }

    //Đăng ký tài khoản mới khi đăng nhập bằng facebook
    private User registerNewUser(OAuth2FbRequest request, Role role) {
        User newUser = User.builder().fullName(request.getFullName()).image(request.getImage()).isActive(EStatus.ACTIVATE.getValue())
                .email(request.getEmail()).status(EStatus.ENABLE.getValue()).roles(Set.of(role)).provider(EProvider.FACEBOOK.getValue())
                .build();

        return userRepository.save(newUser);
    }

    //Cập nhật tài khoản khi tài khoản đã tồn tại
    private User updateExistUser(User existUser, OAuth2FbRequest request) {
        existUser.setFullName(request.getFullName());
        existUser.setImage(request.getImage());

        return userRepository.save(existUser);
    }

    //Tạo mới access-token và refresh-token trả về
    private SigninResponse generateAccessTokenAndRefreshToken(User userLogin) {
        String accessToken = jwtUtil.generateAccessToken(userLogin);
        String refreshToken = iRefreshTokenService.generateRefreshToken(userLogin.getUserId());
        List<String> roles = userLogin.getRoles().stream().map(item -> item.getRoleName().name()).toList();

        return SigninResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                .accessTokenExpirationTime(jwtUtil.getAccessTokenDurationMs()).roles(roles).build();
    }
}
