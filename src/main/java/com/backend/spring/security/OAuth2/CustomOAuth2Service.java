package com.backend.spring.security.OAuth2;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.entities.Role;
import com.backend.spring.entities.User;
import com.backend.spring.enums.EProvider;
import com.backend.spring.enums.ERole;
import com.backend.spring.enums.EStatus;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.exception.OAuth2AuthenticationProcessingException;
import com.backend.spring.repositories.RoleRepository;
import com.backend.spring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(oAuth2User);
        } catch (OAuth2AuthenticationProcessingException ex) {
            throw ex;
        }
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2User);

        if(customOAuth2User.getEmail() == null) {
            throw new OAuth2AuthenticationProcessingException(EStatusCode.OAUTH2_EMAIL_NOT_FOUND.getValue(), MessageConstant.Auth.EMAIL_NOT_FOUND);
        }

        Role roleLearner = roleRepository.findByRoleName(ERole.LEARNER).orElseThrow(() -> new RuntimeException(MessageConstant.Auth.ROLE_NOT_FOUND));

        Optional<User> userOptional = userRepository.findByEmail(customOAuth2User.getEmail());
        Integer provider = customOAuth2User.getAttributes().get("sub") != null ? EProvider.GOOGLE.getValue() : EProvider.FACEBOOK.getValue();

        if(userOptional.isPresent()) {
            User existUser = userOptional.get();
            if(!existUser.getProvider().equals(provider)) {
                throw new OAuth2AuthenticationProcessingException(EStatusCode.OAUTH2_EMAIL_EXISTED.getValue(), MessageConstant.Auth.EMAIL_IS_USED);
            }

            updateExistUser(existUser, customOAuth2User);
        } else {
            registerNewUser(customOAuth2User, roleLearner, provider);
        }

        return customOAuth2User;
    }

    private void registerNewUser(CustomOAuth2User customOAuth2User, Role role, Integer provider) {
        User newUser = User.builder().fullName(customOAuth2User.getName()).image(customOAuth2User.getImage()).isActive(EStatus.ACTIVATE.getValue())
                .email(customOAuth2User.getEmail()).status(EStatus.ENABLE.getValue()).roles(Set.of(role)).provider(provider)
                .build();

        userRepository.save(newUser);
    }

    private void updateExistUser(User existUser, CustomOAuth2User customOAuth2User) {
        existUser.setFullName(customOAuth2User.getName());
        existUser.setImage(customOAuth2User.getImage());

        userRepository.save(existUser);
    }
}
