package com.group2.kahootclone.handler.successHandler;

import com.group2.kahootclone.Utils.JwtUtils;
import com.group2.kahootclone.model.RefreshToken;
import com.group2.kahootclone.model.User;
import com.group2.kahootclone.reposibility.RefreshTokenRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.group2.kahootclone.constant.Providers.GOOGLE;


@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication
            authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userRepository.findUserByEmailAndProvider(oAuth2User.getAttribute("email"), GOOGLE.toString());

        if (user == null) {
            user = userRepository.save(User.builder()
                    .displayName(oAuth2User.getAttribute("name"))
                    .active(true)
                    .email(oAuth2User.getAttribute("email"))
                    .provider(GOOGLE.toString())
                    .build());
        }

        String fehost = request.getHeader("origin");
        String access_token = JwtUtils.genAccessToken(user);
        String refresh_token = JwtUtils.genRefreshToken(user);
        refreshTokenRepository.save(RefreshToken.builder().token(refresh_token).build());
        response.sendRedirect(String.format("%s/oauth2/user?access_token=%s&refresh_token=%s", fehost, access_token, refresh_token));
    }
}
