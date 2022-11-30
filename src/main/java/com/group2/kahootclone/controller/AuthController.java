package com.group2.kahootclone.controller;

import com.group2.kahootclone.Utils.EmailUtils;
import com.group2.kahootclone.Utils.ErrorUtils;
import com.group2.kahootclone.object.EmailDetails;
import com.group2.kahootclone.object.Request.authController.LoginRequest;
import com.group2.kahootclone.object.Request.authController.RegisterRequest;
import com.group2.kahootclone.object.Request.authController.VerificationRequest;
import com.group2.kahootclone.object.Response.authController.LoginResponse;
import com.group2.kahootclone.object.Response.authController.TokenResponse;
import com.group2.kahootclone.object.Response.authController.VerificationResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.service.Interface.IEmailService;
import com.group2.kahootclone.service.Interface.IRefreshTokenService;
import com.group2.kahootclone.service.Interface.IUserService;
import com.group2.kahootclone.service.Interface.IVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @Value("${kahoot.clone.fe}")
    String fehost;

    @PostMapping("refreshToken")
    public ResponseEntity<ResponseObject<TokenResponse>> refreshTokenController(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authorization_header = request.getHeader("AUTHORIZATION");
        ResponseObject<TokenResponse> accessTokenRet = refreshTokenService.refreshToken(authorization_header, (int) authentication.getPrincipal());
        return accessTokenRet.createResponse();
    }

    @PostMapping("login")
    public ResponseEntity<ResponseObject<LoginResponse>> loginController(@RequestBody @Valid LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(token);
        ResponseObject<LoginResponse> loginRet = userService.login(loginRequest);

        return loginRet.createResponse();
    }

    @PostMapping("login/google")
    public ResponseEntity<ResponseObject<LoginResponse>> loginGoogleController(@RequestBody Map<String, Object> tokenMap) {
        ResponseObject<LoginResponse> loginRet = userService.loginWithGoogle((String) tokenMap.get("tokenId"));
        return loginRet.createResponse();
    }


    @PostMapping("register")
    public ResponseEntity<ResponseObject<VerificationResponse>> registerController(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest request) {

        ResponseObject<VerificationResponse> registerUserRet = userService.register(registerRequest);
        if (ErrorUtils.isFail(registerUserRet.getErrorCode())) return registerUserRet.createResponse();

        VerificationResponse verificationResponse = registerUserRet.getObject();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        EmailDetails emailDetails = EmailDetails.builder()
                .attachment("No")
                .recipient(registerRequest.getEmail())
                .msgBody(EmailUtils.buildVerificationTemplate(baseUrl, verificationResponse))
                .subject("Verification")
                .build();
        emailService.sendSimpleEmail(emailDetails);

        return registerUserRet.createResponse();
    }

    @GetMapping("verification")
    public void verification(@RequestParam int verificationId, @RequestParam String code, HttpServletResponse httpResponse) throws IOException {
        VerificationRequest verificationRequest = VerificationRequest.builder().verificationId(verificationId).code(code).build();
        ResponseObject<TokenResponse> tokenRet = verificationService.checkAndVerifyVerification(verificationRequest);
        if (ErrorUtils.isFail(tokenRet.getErrorCode())) {
            httpResponse.sendRedirect(String.format("%s/verification?error=%s", fehost, tokenRet.getMessage()));
            return;
        }
        httpResponse.sendRedirect(String.format("%s/verification?access_token=%s&refresh_token=%s",
                fehost,
                tokenRet.getObject().getAccess_token(),
                tokenRet.getObject().getRefresh_token()));
    }
}
