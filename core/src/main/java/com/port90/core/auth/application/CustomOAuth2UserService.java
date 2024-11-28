package com.port90.core.auth.application;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.auth.dto.request.UserDto;
import com.port90.core.auth.dto.response.GoogleResponse;
import com.port90.core.auth.dto.response.KakaoResponse;
import com.port90.core.auth.dto.response.NaverResponse;
import com.port90.core.auth.dto.response.OAuth2Response;
import com.port90.core.auth.infrastructure.impl.repository.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserRepositoryImpl userRepositoryImpl;

    public CustomOAuth2UserService(UserRepositoryImpl userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = parseOAuth2Response(registrationId, oAuth2User);

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        User existData = userRepositoryImpl.findByUsername(username);

        if (existData == null) {
            // 신규 사용자 처리
            logger.info("New user registered: {}", username);
            User user = User.createUser(username, oAuth2Response.getName(), oAuth2Response.getEmail());
            userRepositoryImpl.save(user);
            return mapToCustomOAuth2User(user);
        } else {
            // 기존 사용자 업데이트 처리
            logger.info("Existing user login: {}", username);
            if (!existData.getName().equals(oAuth2Response.getName()) ||
                    !existData.getEmail().equals(oAuth2Response.getEmail())) {
                logger.info("Updating user info for: {}", username);
                existData.updateNameAndEmail(oAuth2Response.getName(), oAuth2Response.getEmail());
                userRepositoryImpl.save(existData);
            }
            return mapToCustomOAuth2User(existData);
        }
    }

    private CustomOAuth2User mapToCustomOAuth2User(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        return new CustomOAuth2User(userDto);
    }

    private OAuth2Response parseOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        return switch (registrationId) {
            case "naver" -> new NaverResponse(oAuth2User.getAttributes());
            case "google" -> new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        };
    }
}