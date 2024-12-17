package com.estsoft.astronautbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                ).cors(configurer -> {
                    configurer.configurationSource(request -> {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.addAllowedOriginPattern("*"); // 모든 Origin 허용
                        corsConfiguration.addAllowedHeader("*"); // 모든 Header 허용
                        corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용

                        return corsConfiguration;
                    });
                });
        return http.build();
    }
}