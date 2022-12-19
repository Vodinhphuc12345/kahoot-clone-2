package com.group2.kahootclone.handler.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.kahootclone.DTO.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomizedAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseObject responseObject = new ResponseObject();
        response.setContentType(APPLICATION_JSON_VALUE);
        //403
        response.setStatus(HttpStatus.FORBIDDEN.value());
        responseObject.setMessage("You don't have permission for this record");
        responseObject.setObject(null);
        new ObjectMapper().writeValue(response.getOutputStream(), responseObject);
    }
}
