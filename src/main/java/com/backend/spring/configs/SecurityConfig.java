package com.backend.spring.configs;

import com.backend.spring.enums.ERole;
import com.backend.spring.security.OAuth2.CustomOAuth2Service;
import com.backend.spring.security.OAuth2.OAuth2AuthenticationFailureHandler;
import com.backend.spring.security.OAuth2.OAuth2AuthenticationSuccessHandler;
import com.backend.spring.security.jwt.AuthEntryPointJwt;
import com.backend.spring.security.jwt.JwtFilterUtil;

import com.backend.spring.security.service.CustomUserDetailService;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    private final JwtFilterUtil jwtFilterUtil;

    private final CustomOAuth2Service customOAuth2Service;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    //Mã hóa mật khẩu người dùng
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(@Value("${spring.security.oauth2.client.registration.google.client-id}") String cliendId) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(cliendId))
                .build();

    }

    //Quy tắc phần quyền dựa trên URL của tài nguyên.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                        "/v3/api-docs.yaml").permitAll()
                                .requestMatchers("/oauth2/**").permitAll()
                                .requestMatchers("/api/v1/public/**", "/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/admin/**").hasRole(ERole.ADMIN.name())
                                .requestMatchers("/api/v1/user/**").hasAnyRole(ERole.LEARNER.name())
                                .requestMatchers("/ws/**").permitAll()

//                               Cho phép truy cập vào file tĩnh
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/audios/**").permitAll()
                                .requestMatchers("/pdfs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .addFilterBefore(jwtFilterUtil, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
