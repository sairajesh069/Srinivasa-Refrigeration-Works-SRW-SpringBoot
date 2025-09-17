package com.srinivasa.refrigeration.works.srw_springboot.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccessCheck {

    private final JwtUtil jwtUtil;

    public Map<String, String> extractUserIdAndType(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        String userId = jwtUtil.extractUserId(jwt);
        String userType = jwtUtil.extractUserType(jwt).toString();

        return Map.ofEntries(
                Map.entry("UserId", userId),
                Map.entry("UserType", userType)
        );
    }

    public boolean canAccessProfile(String accessedById, HttpServletRequest request) {
        Map<String, String> userDetails = extractUserIdAndType(request);
        return userDetails.get("UserType").equals("OWNER") || accessedById.equals(userDetails.get("UserId"));
    }

    public boolean canAccessComplaints(String accessedById, HttpServletRequest request) {
        Map<String, String> userDetails = extractUserIdAndType(request);
        return userDetails.get("UserType").equals("OWNER") || accessedById.equals(userDetails.get("UserId"));
    }

    public boolean canAccessComplaint(String bookedById, String assignedToId, HttpServletRequest request) {
        Map<String, String> userDetails = extractUserIdAndType(request);
        return userDetails.get("UserType").equals("OWNER")
                || bookedById.equals(userDetails.get("UserId"))
                || (assignedToId != null && assignedToId.equals(userDetails.get("UserId")));
    }

    public boolean canAccessUpdateComplaintState(String assignedToId, HttpServletRequest request) {
        Map<String, String> userDetails = extractUserIdAndType(request);
        return userDetails.get("UserType").equals("OWNER")
                || (userDetails.get("UserType").equals("EMPLOYEE") && assignedToId != null && assignedToId.equals(userDetails.get("UserId")));
    }
}
