package com.group2.kahootclone.object.Response.authController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    String access_token;
    String refresh_token;

    public static TokenResponse fromStrings(String access_token, String refresh_token){
        return TokenResponse
                .builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
    }

}
