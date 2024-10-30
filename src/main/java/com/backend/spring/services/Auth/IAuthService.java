package com.backend.spring.services.Auth;

import com.backend.spring.entities.User;
import com.backend.spring.exception.AlreadyExistsException;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.payload.request.*;
import com.backend.spring.payload.response.SigninResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IAuthService {
    boolean registerUser(SignupRequest signupRequest) throws AlreadyExistsException, NotFoundException;
    boolean signOut(User user);
    Integer verifyAccount(ActivateAccountRequest request);
    Integer sendNewVerificationEmail(EmailRequest emailRequest);
    boolean forgotPasswordMail(EmailRequest request);
    Integer handleResetPassword(ResetPasswordRequest request);
    SigninResponse handleLoginOAuth2Google(String tokenOAuth2) throws GeneralSecurityException, IOException;
    SigninResponse handleLoginOAuth2Facebook(OAuth2FbRequest request);


}
