package com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class SecurityUtil {

    @SuppressWarnings("unchecked")
    public static String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken && auth.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();
            return (String) details.get("userId");
        }
        throw new SecurityException("No authenticated user found");
    }

    @SuppressWarnings("unchecked")
    public static String getCurrentUserType() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken && auth.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();
            return (String) details.get("userType");
        }
        throw new SecurityException("No authenticated user found");
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getName();
        }
        throw new SecurityException("No authenticated user found");
    }

    public static boolean isCurrentUser(String userId) {
        try {
            return getCurrentUserId().equals(userId);
        } catch (SecurityException e) {
            return false;
        }
    }
}