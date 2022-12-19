package com.group2.kahootclone.service.Interface;


import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.DTO.Request.authController.VerificationRequest;
import com.group2.kahootclone.DTO.Response.authController.LoginResponse;
import com.group2.kahootclone.DTO.Response.authController.VerificationResponse;
import com.group2.kahootclone.DTO.ResponseObject;

public interface IVerificationService {
    ResponseObject<VerificationResponse> addVerification(User user);

    ResponseObject<LoginResponse> checkAndVerifyVerification(VerificationRequest verificationRequest);
}
