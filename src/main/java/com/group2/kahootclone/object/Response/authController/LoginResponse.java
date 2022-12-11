package com.group2.kahootclone.object.Response.authController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.object.Response.meController.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginResponse {
    private UserResponse profile;
    private String access_token;
    private String refresh_token;

    public static LoginResponse fromUser(User user){
        UserResponse userResponse = MapperUtil.INSTANCE.map(user, UserResponse.class);
        return LoginResponse.builder().profile(userResponse).build();
    }

    public static LoginResponse fromUserAndToken(User user, String access_token, String refresh_token){
        UserResponse userResponse = MapperUtil.INSTANCE.map(user, UserResponse.class);
        return LoginResponse
                .builder()
                .profile(userResponse)
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
    }
}
