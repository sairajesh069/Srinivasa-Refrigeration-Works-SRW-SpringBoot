package com.srinivasa.refrigeration.works.srw_springboot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srinivasa.refrigeration.works.srw_springboot.service.TokenBlackListService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlackListService tokenBlacklistService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                logger.warn("Attempted to use blacklisted token");

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error: Token has been revoked");
                errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                errorResponse.put("timestamp", LocalDateTime.now().toString());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.warn("Failed to extract username from JWT: " + e.getMessage());
                // Invalid token, continue without authentication
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, username)) {
                try {
                    String userId = jwtUtil.extractUserId(jwt);
                    String userType = jwtUtil.extractUserType(jwt).toString();
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userType));

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    Map<String, Object> details = new HashMap<>();
                    details.put("userId", userId);
                    details.put("userType", userType);
                    details.put("requestDetails", new WebAuthenticationDetailsSource().buildDetails(request));

                    authenticationToken.setDetails(details);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception e) {
                    logger.warn("Failed to extract user details from JWT: " + e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}