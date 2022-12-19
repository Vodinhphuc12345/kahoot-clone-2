package com.group2.kahootclone.controller;

import com.group2.kahootclone.Utils.EmailUtils;
import com.group2.kahootclone.Utils.ErrorUtils;
import com.group2.kahootclone.DTO.EmailDetails;
import com.group2.kahootclone.DTO.Request.authController.LoginRequest;
import com.group2.kahootclone.DTO.Request.authController.PasswordConfirmationRequest;
import com.group2.kahootclone.DTO.Request.authController.RegisterRequest;
import com.group2.kahootclone.DTO.Request.authController.VerificationRequest;
import com.group2.kahootclone.DTO.Response.authController.LoginResponse;
import com.group2.kahootclone.DTO.Response.authController.PasswordConfirmationResponse;
import com.group2.kahootclone.DTO.Response.authController.TokenResponse;
import com.group2.kahootclone.DTO.Response.authController.VerificationResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.service.Interface.IEmailService;
import com.group2.kahootclone.service.Interface.IRefreshTokenService;
import com.group2.kahootclone.service.Interface.IUserService;
import com.group2.kahootclone.service.Interface.IVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    IUserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    IRefreshTokenService refreshTokenService;
    @Autowired
    IVerificationService verificationService;
    @Autowired
    IEmailService emailService;

    @PostMapping("refreshToken")
    public ResponseEntity<ResponseObject<TokenResponse>> refreshTokenController(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authorization_header = request.getHeader("AUTHORIZATION");
        ResponseObject<TokenResponse> accessTokenRet = refreshTokenService.refreshToken(authorization_header, (int) authentication.getPrincipal());
        return accessTokenRet.createResponse();
    }

    @PostMapping("login")
    public ResponseEntity<ResponseObject<LoginResponse>> loginController(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(token);
        ResponseObject<LoginResponse> loginRet = userService.login(loginRequest);

        return loginRet.createResponse();
    }

    @PostMapping("password/renew")
    public ResponseEntity<ResponseObject<PasswordConfirmationResponse>> renewController(@RequestBody @Valid PasswordConfirmationRequest passwordConfirmationRequest,
                                                                                        HttpServletRequest request) {
        ResponseObject<PasswordConfirmationResponse> confirmationRes = userService.renew(passwordConfirmationRequest);

        if (ErrorUtils.isFail(confirmationRes.getErrorCode()))
            return confirmationRes.createResponse();

        PasswordConfirmationResponse response = confirmationRes.getObject();

        String fehost = request.getHeader("origin");
        EmailDetails emailDetails = EmailDetails.builder()
                .attachment("No")
                .recipient(passwordConfirmationRequest.getEmail())
                .msgBody(EmailUtils.buildPasswordConfirmationTemplate(fehost, response))
                .subject("Confirmation")
                .build();
        emailService.sendSimpleEmail(emailDetails);
        return confirmationRes.createResponse();
    }

    @GetMapping("password/confirmation/{code}")
    public ResponseEntity<ResponseObject<LoginResponse>> confirmPassword(@PathVariable String code,
                                                                                        HttpServletRequest request) {
        ResponseObject<LoginResponse> confirmationRes = userService.confirmPassword(code);
        return confirmationRes.createResponse();
    }

    @PostMapping("login/google")
    public ResponseEntity<ResponseObject<LoginResponse>> loginGoogleController(@RequestBody Map<String, Object> tokenMap) {
        ResponseObject<LoginResponse> loginRet = userService.loginWithGoogle((String) tokenMap.get("tokenId"));
        return loginRet.createResponse();
    }


    @PostMapping("register")
    public ResponseEntity<ResponseObject<VerificationResponse>> registerController(@Valid @RequestBody RegisterRequest registerRequest,
                                                                                   HttpServletRequest request) {

        ResponseObject<VerificationResponse> registerUserRet = userService.register(registerRequest);
        if (ErrorUtils.isFail(registerUserRet.getErrorCode())) return registerUserRet.createResponse();

        VerificationResponse verificationResponse = registerUserRet.getObject();
        String fehost = request.getHeader("origin");
        EmailDetails emailDetails = EmailDetails.builder()
                .attachment("No")
                .recipient(registerRequest.getEmail())
                .msgBody(EmailUtils.buildVerificationTemplate(fehost, verificationResponse))
                .subject("Verification")
                .build();
        emailService.sendSimpleEmail(emailDetails);

        return registerUserRet.createResponse();
    }

    @GetMapping("verification")
    public ResponseEntity<ResponseObject<LoginResponse>> verification(@RequestParam int verificationId, @RequestParam String code,
                                                                      HttpServletRequest httpRequest,
                                                                      HttpServletResponse httpResponse) throws IOException {
        String fehost = httpRequest.getHeader("origin");
        VerificationRequest verificationRequest = VerificationRequest.builder().verificationId(verificationId).code(code).build();
        ResponseObject<LoginResponse> verificationRes = verificationService.checkAndVerifyVerification(verificationRequest);

        return verificationRes.createResponse();

    }
}
