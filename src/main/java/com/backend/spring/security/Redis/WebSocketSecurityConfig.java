package com.backend.spring.security.Redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

//@Configuration
//@EnableWebSocketSecurity
//public class WebSocketSecurityConfig {
//    @Bean
//    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//        messages
//                .simpDestMatchers("/comment/add-new").permitAll()// Chỉ cho phép người dùng có vai trò USER gửi đến /comment/**
//                .simpDestMatchers("/app/user/comment/create").permitAll()// Chỉ cho phép người dùng có vai trò USER gửi đến /comment/**
//                .anyMessage().denyAll();
//
//        return messages.build();
//    }
//}
