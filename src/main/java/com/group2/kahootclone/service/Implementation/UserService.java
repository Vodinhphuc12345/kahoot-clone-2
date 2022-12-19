package com.group2.kahootclone.service.Implementation;

import com.cloudinary.utils.ObjectUtils;
import com.group2.kahootclone.Utils.GoogleVerifier;
import com.group2.kahootclone.Utils.JwtUtils;
import com.group2.kahootclone.constant.ErrorCodes;
import com.group2.kahootclone.constant.ExpiredTimes;
import com.group2.kahootclone.mapper.UserMapper;
import com.group2.kahootclone.model.auth.PasswordConfirmation;
import com.group2.kahootclone.model.auth.RefreshToken;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.auth.Verification;
import com.group2.kahootclone.DTO.Request.authController.LoginRequest;
import com.group2.kahootclone.DTO.Request.authController.PasswordConfirmationRequest;
import com.group2.kahootclone.DTO.Request.authController.RegisterRequest;
import com.group2.kahootclone.DTO.Response.authController.LoginResponse;
import com.group2.kahootclone.DTO.Response.authController.PasswordConfirmationResponse;
import com.group2.kahootclone.DTO.Response.authController.VerificationResponse;
import com.group2.kahootclone.DTO.Response.groupController.KahootGroupResponse;
import com.group2.kahootclone.DTO.Response.meController.UserResponse;
import com.group2.kahootclone.DTO.ResponseObject;
import com.group2.kahootclone.repository.PasswordConfirmationRepository;
import com.group2.kahootclone.repository.RefreshTokenRepository;
import com.group2.kahootclone.repository.UserRepository;
import com.group2.kahootclone.repository.VerificationRepository;
import com.group2.kahootclone.service.Interface.ICloudinaryService;
import com.group2.kahootclone.service.Interface.IUserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.group2.kahootclone.constant.Providers.GOOGLE;

@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    PasswordConfirmationRepository passwordConfirmationRepository;

    @Autowired
    ICloudinaryService cloudinaryService;

    @Autowired
    GoogleVerifier googleVerifier;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUsersByUsernameAndActive(username, true);
        if (user == null) {
            throw new UsernameNotFoundException("User name is not found");
        } else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        }
    }

    @Override
    public ResponseObject<UserResponse> saveUser(RegisterRequest registerRequest) {
        ResponseObject<UserResponse> ret = new ResponseObject<>();
        try {
            registerRequest.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
            User user = userRepository.save(registerRequest.toUser());

            //build success
            ret.setObject(UserResponse.fromUser(user));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<UserResponse> findUserByUsername(String username) {
        ResponseObject<UserResponse> ret = new ResponseObject<>();
        try {
            User user = userRepository.findUsersByUsername(username);

            //build resource not found
            if (user == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }

            //build success
            ret.setObject(UserResponse.fromUser(user));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<UserResponse> findActiveUserByUsername(String username) {
        ResponseObject<UserResponse> ret = new ResponseObject<>();
        try {
            User user = userRepository.findUsersByUsernameAndActive(username, true);

            //build resource not found
            if (user == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }
            //build success
            ret.setObject(UserResponse.fromUser(user));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<UserResponse> findUser(int userId) {
        ResponseObject<UserResponse> ret = new ResponseObject<>();
        try {
            Optional<User> userRet = userRepository.findById(userId);
            User user = userRet.isEmpty() ? null : userRet.get();
            //build resource not found
            if (user == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }
            //build success
            ret.setObject(UserResponse.fromUser(user));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<VerificationResponse> register(RegisterRequest registerRequest) {
        ResponseObject<VerificationResponse> ret = new ResponseObject<>();
        try {
            User curUser = userRepository.findUserByUsername(registerRequest.getUsername());
            if (curUser != null) {
                ret.setMessage("User is exited.");
                ret.setErrorCode(ErrorCodes.EXISTED);
                return ret;
            }
            User user = registerRequest.toUser();
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            //build resource not found
            //save
            user = userRepository.save(user);

            //add verification
            String OTP = RandomString.make(8);
            Verification verification = new Verification();
            verification.setCode(OTP);
            verification.setUser(user);
            verification.setTimeExpired(System.currentTimeMillis() + ExpiredTimes.VERIFICATION_TOKEN_EXPIRATION);

            Verification savedVerification = verificationRepository.save(verification);
            //build success
            ret.setObject(VerificationResponse.fromVerification(savedVerification));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<UserResponse> uploadAvatar(int userId, MultipartFile image) {
        ResponseObject<UserResponse> ret = new ResponseObject<>();
        try {
            Optional<User> userRet = userRepository.findById(userId);

            User curUser = userRet.isEmpty() ? null : userRet.get();
            if (curUser == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }
            //upload image
            Map params = ObjectUtils.asMap("public_id", "kahootClone/avatar/" + image.getOriginalFilename().split("\\.", 3)[0]);
            Map<String, Object> cloudinaryUrl = cloudinaryService.upload(image, params);

            if (cloudinaryUrl == null || cloudinaryUrl.get("url") == null) {
                ret.setMessage("Saving avatar to storage is failed.");
                ret.setErrorCode(ErrorCodes.WRITE_FAILED);
                return ret;
            }

            String avatarUrl = (String) cloudinaryUrl.get("url");

            //save to user
            curUser.setAvatar(avatarUrl);
            curUser = userRepository.save(curUser);

            //build success
            ret.setObject(UserResponse.fromUser(curUser));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<UserResponse> updateProfile(int userId, RegisterRequest updateRequest) {
        ResponseObject<UserResponse> ret = new ResponseObject<>();
        try {
            Optional<User> userRet = userRepository.findById(userId);

            User curUser = userRet.orElse(null);
            if (curUser == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }

            //update to user
            if (updateRequest.getPassword() != null)
                updateRequest.setPassword(new BCryptPasswordEncoder().encode(updateRequest.getPassword()));
            Mappers.getMapper(UserMapper.class).registerRequestToUser(updateRequest, curUser);
            curUser = userRepository.save(curUser);

            //build success
            ret.setObject(UserResponse.fromUser(curUser));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<LoginResponse> loginWithGoogle(String tokenId) {
        ResponseObject<LoginResponse> ret = new ResponseObject<>();
        try {
            User user = googleVerifier.verifyToken(tokenId);
            if (user == null) {
                ret.setMessage("Verify google account get failure.");
                ret.setErrorCode(ErrorCodes.RESOURCE_NOT_FOUND);
                return ret;
            }

            User curUser = userRepository.findUserByEmailAndProvider(user.getEmail(), GOOGLE.toString());
            //save user
            if (curUser == null) {
                user.setProvider(GOOGLE.toString());
                user = userRepository.save(user);
            } else {
                user = curUser;
            }

            String access_token = JwtUtils.genAccessToken(user);
            String refresh_token = JwtUtils.genRefreshToken(user);

            refreshTokenRepository.save(RefreshToken.builder().token(refresh_token).build());
            //build success
            ret.setObject(LoginResponse.fromUserAndToken(user, access_token, refresh_token));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<LoginResponse> login(LoginRequest loginRequest) {
        ResponseObject<LoginResponse> ret = new ResponseObject<>();
        try {
            User user = userRepository.findUsersByUsername(loginRequest.getUsername());
            if (user == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }

            String access_token = JwtUtils.genAccessToken(user);
            String refresh_token = JwtUtils.genRefreshToken(user);

            refreshTokenRepository.save(RefreshToken.builder().token(refresh_token).build());
            //build success
            ret.setObject(LoginResponse.fromUserAndToken(user, access_token, refresh_token));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<List<KahootGroupResponse>> getAllGroupOfUser(int userId) {
        ResponseObject<List<KahootGroupResponse>> ret = new ResponseObject<>();
        try {
            Optional<User> userRet = userRepository.findById(userId);
            User curUser = userRet.isEmpty() ? null : userRet.get();
            if (curUser == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }

            //get data

            List<KahootGroupResponse> groups = curUser.getUserKahootGroups().stream().map(ukp -> {
                KahootGroupResponse kahootGroupResponse = KahootGroupResponse.fromKahootGroup(ukp.getKahootGroup());
                kahootGroupResponse.setRole(ukp.getRole());
                return kahootGroupResponse;
            }).collect(Collectors.toList());
            //build success
            ret.setObject(groups);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<PasswordConfirmationResponse> renew(PasswordConfirmationRequest passwordConfirmationRequest) {
        ResponseObject<PasswordConfirmationResponse> ret = new ResponseObject<>();
        try {
            User curUser = userRepository
                    .findUsersByUsernameAndEmail(passwordConfirmationRequest.getUsername()
                            , passwordConfirmationRequest.getEmail());
            if (curUser == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }
            //convert password request
            PasswordConfirmation passwordConfirmation = passwordConfirmationRequest.toPasswordConfirmation();
            passwordConfirmation.setUser(curUser);
            PasswordConfirmation savedPasswordConfirmation = passwordConfirmationRepository.save(passwordConfirmation);
            //build success
            ret.setObject(PasswordConfirmationResponse.fromPasswordConfirmation(savedPasswordConfirmation));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<LoginResponse> confirmPassword(String code) {
        ResponseObject<LoginResponse> ret = new ResponseObject<>();
        try {
            PasswordConfirmation passwordConfirmation = passwordConfirmationRepository.findPasswordConfirmationByCode(code);
            if (passwordConfirmation == null) {
                ret.buildResourceNotFound("Password Confirmation is not found.");
                return ret;
            }
            if (passwordConfirmation.getUser() == null) {
                ret.buildResourceNotFound("User is not found.");
                return ret;
            }
            if (passwordConfirmation.isClicked()) {
                ret.buildResourceNotFound("This request have been confirmed before.");
                return ret;
            }
            if (passwordConfirmation.getTimeExpired() < System.currentTimeMillis()) {
                ret.buildResourceNotFound("This request is expired.");
                return ret;
            }
            //convert password request
            // update user
            User curUser = passwordConfirmation.getUser();
            String newPassword = new BCryptPasswordEncoder().encode(passwordConfirmation.getPassword());
            curUser.setPassword(newPassword);
            User savedUser = userRepository.save(curUser);

            // update confirmation
            passwordConfirmation.setClicked(true);
            passwordConfirmationRepository.save(passwordConfirmation);

            // token
            String access_token = JwtUtils.genAccessToken(curUser);
            String refresh_token = JwtUtils.genRefreshToken(curUser);
            //save refresh token
            refreshTokenRepository.save(RefreshToken.builder().token(refresh_token).build());
            // get token
            //build success
            ret.setObject(LoginResponse.fromUserAndToken(curUser, access_token, refresh_token));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
