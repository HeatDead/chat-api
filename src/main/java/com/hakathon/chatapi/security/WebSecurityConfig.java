package com.hakathon.chatapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@CrossOrigin("*")
public class WebSecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/chat/*","/registration/*", "/fetchAllUsers").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/movies", "/api/movies/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .requestMatchers("/api/movies/*/comments").hasAnyRole(CHAT_MANAGER, USER)
                .requestMatchers("/api/movies", "/api/movies/**").hasRole(CHAT_MANAGER)
                .requestMatchers("/api/userextras/me").hasAnyRole(CHAT_MANAGER, USER)
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().and().csrf().disable();
        return http.build();
    }

    public static final String CHAT_MANAGER = "CHAT_MANAGER";
    public static final String USER = "USER";
}