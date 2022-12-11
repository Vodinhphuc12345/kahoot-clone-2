package com.group2.kahootclone.service.Interface;


import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.object.Request.authController.VerificationRequest;
import com.group2.kahootclone.object.Response.authController.LoginResponse;
import com.group2.kahootclone.object.Response.authController.VerificationResponse;
import com.group2.kahootclone.object.ResponseObject;

public interface IVerificationService {
    ResponseObject<VerificationResponse> addVerification(User user);

    ResponseObject<LoginResponse> checkAndVerifyVerification(VerificationRequest verificationRequest);
}
