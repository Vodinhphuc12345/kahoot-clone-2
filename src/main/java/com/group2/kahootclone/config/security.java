package com.group2.kahootclone.config;

import com.group2.kahootclone.handler.exceptionHandler.AuthenticationExceptionEntryPoint;
import com.group2.kahootclone.handler.exceptionHandler.CustomizedAccessDeniedHandler;
import com.group2.kahootclone.filter.JWTFilter;
import com.group2.kahootclone.service.Interface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.group2.kahootclone.constant.WhiteLists.AUTH_WHITELIST;
import static com.group2.kahootclone.constant.WhiteLists.SWAGGER_WHITELIST;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class security extends WebSecurityConfigurerAdapter {
    IUserService userService;
    AuthenticationExceptionEntryPoint authenticationExceptionEntryPoint;
    CustomizedAccessDeniedHandler customizedAccessDeniedHandler;
    JWTFilter jwtFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.
                cors();
        http
                .authorizeRequests()
                .antMatchers(SWAGGER_WHITELIST).permitAll();
        http
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll();

        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .successHandler(authenticationSuccessHandler);

        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionEntryPoint)
                .accessDeniedHandler(customizedAccessDeniedHandler);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
