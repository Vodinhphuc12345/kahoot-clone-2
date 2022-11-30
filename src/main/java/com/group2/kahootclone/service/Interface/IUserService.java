package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.object.Request.authController.LoginRequest;
import com.group2.kahootclone.object.Request.authController.RegisterRequest;
import com.group2.kahootclone.object.Response.authController.LoginResponse;
import com.group2.kahootclone.object.Response.authController.VerificationResponse;
import com.group2.kahootclone.object.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import com.group2.kahootclone.object.ResponseObject;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService extends UserDetailsService {
    ResponseObject<UserResponse> saveUser(RegisterRequest registerRequest);
    ResponseObject<UserResponse> findUserByUsername (String username);
    ResponseObject<UserResponse> findActiveUserByUsername (String username);
    ResponseObject<UserResponse> findUser(int userId);

    ResponseObject<VerificationResponse> register(RegisterRequest registerRequest);

    ResponseObject<UserResponse> uploadAvatar(int userId, MultipartFile image);

    ResponseObject<UserResponse> updateProfile(int userId, RegisterRequest updateRequest);

    ResponseObject<LoginResponse> loginWithGoogle(String tokenId);
    ResponseObject<LoginResponse> login(LoginRequest loginRequest);

    ResponseObject<List<KahootGroupResponse>> getAllGroupOfUser(int userId);
}
