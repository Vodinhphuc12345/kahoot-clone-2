package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.Utils.JwtUtils;
import com.group2.kahootclone.constant.ExpiredTimes;
import com.group2.kahootclone.model.auth.RefreshToken;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.auth.Verification;
import com.group2.kahootclone.object.Request.authController.VerificationRequest;
import com.group2.kahootclone.object.Response.authController.LoginResponse;
import com.group2.kahootclone.object.Response.authController.VerificationResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.RefreshTokenRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import com.group2.kahootclone.reposibility.VerificationRepository;
import com.group2.kahootclone.service.Interface.IVerificationService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VerificationService implements IVerificationService {
    @Autowired
    VerificationRepository verificationRepo;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public ResponseObject<VerificationResponse> addVerification(User user) {
        ResponseObject<VerificationResponse> ret = new ResponseObject<>();
        try {
            String OTP = RandomString.make(8);
            Verification verification = new Verification();
            verification.setCode(OTP);
            verification.setUser(user);
            verification.setTimeExpired(System.currentTimeMillis() + ExpiredTimes.VERIFICATION_TOKEN_EXPIRATION);
            Verification savedVerification = verificationRepo.save(verification);

            //build success
            ret.setObject(VerificationResponse.fromVerification(savedVerification));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }


    @Override
    public ResponseObject<LoginResponse> checkAndVerifyVerification(VerificationRequest verificationRequest) {

        ResponseObject<LoginResponse> ret = new ResponseObject<>();
        try {
            Optional<Verification> verificationRet = verificationRepo.findById(verificationRequest.getVerificationId());
            Verification verification = verificationRet.orElse(null);

            //build resource not found
            if (verification == null) {
                ret.buildResourceNotFound("Verification is failed");
                return ret;
            }
            //build expired resource
            if (verification.getTimeExpired() < System.currentTimeMillis()) {
                ret.buildResourceNotFound("Verification is expired");
                return ret;
            }

            if (!verification.getCode().equals(verificationRequest.getCode())) {
                ret.buildResourceNotFound("Verification is failed");
                return ret;
            }

            //build success
            User user = verification.getUser();
            user.setActive(true);
            user = userRepository.save(user);

            String access_token = JwtUtils.genAccessToken(user);
            String refresh_token = JwtUtils.genRefreshToken(user);
            //save refresh token
            refreshTokenRepository.save(RefreshToken.builder().token(refresh_token).build());

            ret.setObject(LoginResponse.fromUserAndToken(user, access_token, refresh_token));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
