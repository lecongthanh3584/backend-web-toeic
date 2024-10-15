package com.backend.spring.services.Auth;

import com.backend.spring.entities.User;
import com.backend.spring.exception.AlreadyExistsException;
import com.backend.spring.exception.NotFoundException;
import com.backend.spring.payload.request.ActivateAccountRequest;
import com.backend.spring.payload.request.EmailRequest;
import com.backend.spring.payload.request.ResetPasswordRequest;
import com.backend.spring.payload.request.SignupRequest;

public interface IAuthService {
    boolean registerUser(SignupRequest signupRequest) throws AlreadyExistsException, NotFoundException;
    boolean signOut(User user);
    Integer verifyAccount(ActivateAccountRequest request);
    Integer sendNewVerificationEmail(EmailRequest emailRequest);
    boolean forgotPasswordMail(EmailRequest request);
    Integer handleResetPassword(ResetPasswordRequest request);
}
