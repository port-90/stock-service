package com.port90.core.auth.application;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.dto.request.CustomOAuth2User;
import com.port90.core.auth.dto.request.UserDto;
import com.port90.core.auth.dto.response.GoogleResponse;
import com.port90.core.auth.dto.response.KakaoResponse;
import com.port90.core.auth.dto.response.NaverResponse;
import com.port90.core.auth.dto.response.OAuth2Response;
import com.port90.core.auth.infrastructure.impl.repository.UserRepositoryImpl;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepositoryImpl userRepositoryImpl;

    public CustomOAuth2UserService(UserRepositoryImpl userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        User existData = userRepositoryImpl.findByUsername(username);

        if (existData == null) {

            User user = User.createUser(username, oAuth2Response.getName(), oAuth2Response.getEmail());
            userRepositoryImpl.save(user);

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setName(oAuth2Response.getName());
            userDto.setRole("ROLE_USER");

            return new CustomOAuth2User(userDto);
        } else {

            existData.updateNameAndEmail(oAuth2Response.getName(), oAuth2Response.getEmail());
            userRepositoryImpl.save(existData);

            UserDto userDto = new UserDto();
            userDto.setUsername(existData.getUsername());
            userDto.setName(oAuth2Response.getName());
            userDto.setRole(existData.getRole());

            return new CustomOAuth2User(userDto);
        }
    }
}
