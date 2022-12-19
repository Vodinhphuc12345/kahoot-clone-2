package com.group2.kahootclone.handler.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.DTO.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ResponseObject responseObject = new ResponseObject();
        response.setContentType(APPLICATION_JSON_VALUE);
        // 401
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseObject.setMessage("Your authentication is wrong");
        responseObject.setObject(null);
        new ObjectMapper().writeValue(response.getOutputStream(), responseObject);
    }
}
