package com.backend.spring.security.OAuth2;

import com.backend.spring.enums.ERole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(ERole.LEARNER.name()));
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    public String getImage() {
        // Kiểm tra nguồn gốc đăng nhập
        if (oAuth2User.getAttributes().containsKey("id")) { // Facebook
            Map<String, Object> pictureData = oAuth2User.getAttribute("picture");
            Map<String, Object> data = (Map<String, Object>) pictureData.get("data");
            return (String) data.get("url");

        } else { // Google
            return oAuth2User.getAttribute("picture");
        }
    }
}
