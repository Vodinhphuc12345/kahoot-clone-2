package com.group2.kahootclone.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.group2.kahootclone.constant.ExpiredTimes;
import com.group2.kahootclone.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtils {

    public static String key="vodinhphuc";
    public static Algorithm algorithm = Algorithm.HMAC256(key);

    public static int verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return Integer.parseInt(decodedJWT.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }

    }

    public static String genRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getId()+"")
                .withExpiresAt(new Date(System.currentTimeMillis() + ExpiredTimes.REFRESH_TOKEN_EXPIRATION))
                .sign(algorithm);
    }

    public static String genAccessToken(User user) {
        return JWT.create()
                        .withSubject(user.getId()+"")
                        .withExpiresAt(new Date(System.currentTimeMillis() + ExpiredTimes.ACCESS_TOKEN_EXPIRATION))
                        .sign(algorithm);
    }
}
