package com.srinivasa.refrigeration.works.srw_springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/srw/home").permitAll()
                        .anyRequest().authenticated())
                .build();
    }
}