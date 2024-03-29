package com.group2.kahootclone.service.Interface;

import com.group2.kahootclone.model.RefreshToken;
import com.group2.kahootclone.object.Response.authController.TokenResponse;
import com.group2.kahootclone.object.ResponseObject;

import java.util.Map;

public interface IRefreshTokenService {
    ResponseObject<Boolean> verifyRefreshToken(String token);
    ResponseObject<RefreshToken> saveRefreshToken (RefreshToken token);

    ResponseObject<Boolean> deleteRefreshToken(String refresh_token);

    ResponseObject<TokenResponse> refreshToken(String authorization_header, int userId);
}
