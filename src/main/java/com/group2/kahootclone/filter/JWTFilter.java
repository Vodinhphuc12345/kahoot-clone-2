package com.group2.kahootclone.filter;

import com.group2.kahootclone.Utils.JwtUtils;
import com.group2.kahootclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("AUTHORIZATION");
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] infos = authorizationHeader.split(" ");
        if (infos.length != 2 || !infos[0].equals("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = infos[1];

        try {
            int userId = JwtUtils.verifyToken(token);

            if (userId > 0) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userId, null,
                                null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception exception) {
            filterChain.doFilter(request, response);
        }
    }
}
