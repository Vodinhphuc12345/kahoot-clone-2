package com.group2.kahootclone.constant;

public class WhiteLists {
    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/swagger-ui/**",
            "/.well-known/pki-validation/**"

    };
    public static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/login/google",
            "/api/v1/auth/register",
            "/api/v1/auth/verification",
            "/api/v1/auth/verification/resend",
            "/socket",
            "/api/v1/auth/password/renew",
            "/api/v1/auth/password/confirmation/*"
    };
}
