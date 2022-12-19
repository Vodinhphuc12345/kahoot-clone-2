package com.group2.kahootclone.controller;

import com.group2.kahootclone.DTO.Request.authController.RegisterRequest;
import com.group2.kahootclone.DTO.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.Response.presentationController.PresentationResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.service.Interface.IKahootGroupService;
import com.group2.kahootclone.service.Interface.IPresentationService;
import com.group2.kahootclone.service.Interface.IRefreshTokenService;
import com.group2.kahootclone.service.Interface.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/me")
public class MeController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    IUserService userService;
    @Autowired
    IKahootGroupService kahootGroupService;
    @Autowired
    IRefreshTokenService refreshTokenService;
    @Autowired
    IPresentationService presentationService;

    @GetMapping
    public ResponseEntity<ResponseObject<UserResponse>> getProfile() {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<UserResponse> userRet = userService.findUser(userId);
        return userRet.createResponse();
    }

    @GetMapping("/groups")
    public ResponseEntity<ResponseObject<List<KahootGroupResponse>>> getAllGroups() {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<List<KahootGroupResponse>> listGroupResponse = userService.getAllGroupOfUser(userId);

        return listGroupResponse.createResponse();
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<ResponseObject<UserResponse>> uploadAvatar(@RequestBody MultipartFile image) {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<UserResponse> userRet = userService.uploadAvatar(userId, image);
        return userRet.createResponse();
    }

    @PostMapping("logout")
    public ResponseEntity<ResponseObject<Boolean>> logoutController(HttpServletRequest request) {
        String authorization_header = request.getHeader("AUTHORIZATION");
        ResponseObject<Boolean> logoutRet = refreshTokenService.deleteRefreshToken(authorization_header);
        return logoutRet.createResponse();
    }

    @PutMapping("")
    public ResponseEntity<ResponseObject<UserResponse>> updateProfile(@RequestBody RegisterRequest updateRequest) {
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<UserResponse> updateRet = userService.updateProfile(userId, updateRequest);
        return updateRet.createResponse();
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseObject<Boolean>> validateMe() {
        return ResponseEntity.ok(ResponseObject.<Boolean>builder().build());
    }
    @GetMapping ("/presentation")
    public  ResponseEntity<ResponseObject<List<PresentationResponse>>> getMyPresentation (){
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<List<PresentationResponse>> presentationRes = presentationService.getPresentationsOfUser(userId);
        return presentationRes.createResponse();
    }
    @GetMapping ("/presentation/collaboration")
    public  ResponseEntity<ResponseObject<List<PresentationResponse>>> getMyCollaborationPresentation (){
        int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject<List<PresentationResponse>> presentationRes = presentationService.getCollaborationPresentationsOfUser(userId);
        return presentationRes.createResponse();
    }
}
