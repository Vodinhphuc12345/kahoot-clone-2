package com.group2.kahootclone.service.Implementation;

import com.group2.kahootclone.Utils.JwtUtils;
import com.group2.kahootclone.model.RefreshToken;
import com.group2.kahootclone.model.User;
import com.group2.kahootclone.object.Response.authController.TokenResponse;
import com.group2.kahootclone.object.ResponseObject;
import com.group2.kahootclone.reposibility.RefreshTokenRepository;
import com.group2.kahootclone.service.Interface.IRefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RefreshTokenService implements IRefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public ResponseObject<Boolean> verifyRefreshToken(String token) {
        ResponseObject<Boolean> ret = new ResponseObject<>();
        try {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
            //build resource not found
            if (refreshToken == null) {
                ret.buildResourceNotFound("Refresh token is invalid.");
                return ret;
            }
            int userId = JwtUtils.verifyToken(token);
            ret.setObject(userId > 0);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<RefreshToken> saveRefreshToken(RefreshToken token) {
        ResponseObject<RefreshToken> ret = new ResponseObject<>();
        try {
            RefreshToken refreshToken = refreshTokenRepository.save(token);
            //build success
            ret.setObject(refreshToken);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            ret.buildException(exception.getMessage());
        }
        return ret;

    }

    @Transactional
    @Override
    public ResponseObject<Boolean> deleteRefreshToken(String authorization_header) {
        ResponseObject<Boolean> ret = new ResponseObject<>();
        try {
            String[] headers = authorization_header.split(" ");
            RefreshToken refreshToken = refreshTokenRepository.findByToken(headers[1]);
            //build resource not found
            if (refreshToken == null) {
                ret.buildResourceNotFound("Refresh token is invalid.");
                return ret;
            }
            //deleting
            refreshTokenRepository.deleteRefreshTokensByToken(refreshToken.getToken());
            //build success
            ret.setObject(true);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            ret.buildException(exception.getMessage());
        }
        return ret;
    }

    @Override
    public ResponseObject<TokenResponse> refreshToken(String authorization_header, int userId) {
        ResponseObject<TokenResponse> ret = new ResponseObject<>();
        try {
            String[] headers = authorization_header.split(" ");
            RefreshToken refreshToken = refreshTokenRepository.findByToken(headers[1]);
            //build resource not found
            if (refreshToken == null) {
                ret.buildResourceNotFound("Refresh token is invalid.");
                return ret;
            }
            //gen access token
            String access_token = JwtUtils.genAccessToken(User.builder().id(userId).build());
            Map<String, String> map = new HashMap<>();
            map.put("access_token", access_token);
            //build success
            ret.setObject(TokenResponse.fromStrings(access_token,headers[1]));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            ret.buildException(exception.getMessage());
        }
        return ret;
    }
}
