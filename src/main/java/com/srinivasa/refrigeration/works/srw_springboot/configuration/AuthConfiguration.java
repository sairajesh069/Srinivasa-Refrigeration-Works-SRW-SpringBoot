package com.srinivasa.refrigeration.works.srw_springboot.configuration;

import com.srinivasa.refrigeration.works.srw_springboot.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/srw/home", "/srw/login").permitAll()
                        .requestMatchers("/srw/forgot-username", "/srw/validate-user", "/srw/forgot-password").permitAll()
                        .requestMatchers("/srw/customer/register").permitAll()
                        .requestMatchers("/srw/owner/register", "/srw/employee/register").hasRole("OWNER")
                        .requestMatchers("/srw/owner/profile", "/srw/owner/update-profile").hasRole("OWNER")
                        .requestMatchers("/srw/employee/profile", "/srw/employee/update-profile").hasAnyRole("OWNER", "EMPLOYEE")
                        .requestMatchers("/srw/customer/profile", "/srw/customer/update-profile").hasAnyRole("OWNER", "CUSTOMER")
                        .requestMatchers("/srw/user/fetch-username", "/srw/user/change-password").hasAnyRole("CUSTOMER", "EMPLOYEE", "OWNER")
                        .requestMatchers("/srw/complaint/register", "/srw/complaint/raised-by", "/srw/complaint/by-id", "/srw/complaint/update", "/srw/complaint/resolved-list", "/srw/complaint/user-feedback").hasAnyRole("CUSTOMER", "EMPLOYEE", "OWNER")
                        .requestMatchers("/srw/complaint/list", "/srw/employee/active-list").hasRole("OWNER")
                        .requestMatchers("/srw/complaint/assigned-to").hasAnyRole("EMPLOYEE", "OWNER")
                        .requestMatchers("/srw/owner/list", "/srw/employee/list", "/srw/customer/list").hasRole("OWNER")
                        .requestMatchers("/srw/owner/update-status", "/srw/employee/update-status", "/srw/customer/update-status").hasRole("OWNER")
                        .requestMatchers("/srw/complaint/update-state").hasAnyRole("CUSTOMER", "EMPLOYEE", "OWNER")
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}