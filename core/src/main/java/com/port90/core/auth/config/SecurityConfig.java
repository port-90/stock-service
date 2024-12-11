package com.port90.core.auth.config;

import com.port90.core.auth.application.CustomOAuth2UserService;
import com.port90.core.auth.jwt.JWTFilter;
import com.port90.core.auth.jwt.JWTUtil;
import com.port90.core.auth.oauth2.CustomLogoutSuccessHandler;
import com.port90.core.auth.oauth2.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, CustomLogoutSuccessHandler customLogoutSuccessHandler, JWTUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)      // CSRF 비활성화
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("*"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }))
                .formLogin(AbstractHttpConfigurer::disable)      // Form 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)      // HTTP Basic 인증 비활성화
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)
                .oauth2Login((oauth2) -> oauth2     // OAuth2 로그인 설정
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))
                .authorizeHttpRequests(auth -> auth             // 경로별 인가 설정
                        .requestMatchers("/", "/login", "/api/ranks/*", "/comments/guests/**", "/error")
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/comments/**"))
                        .permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/logout")                           // 로그아웃 URL
                        .logoutSuccessHandler(customLogoutSuccessHandler) // 성공 핸들러 추가
                        .deleteCookies("Authorization")  // JWT 삭제
                        .invalidateHttpSession(true)                     // 세션 무효화
                )
                .sessionManagement(session ->                   // 세션 설정: STATELESS
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}

