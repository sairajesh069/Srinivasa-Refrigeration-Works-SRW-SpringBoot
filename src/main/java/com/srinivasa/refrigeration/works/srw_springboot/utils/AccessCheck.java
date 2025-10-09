package com.srinivasa.refrigeration.works.srw_springboot.utils;

import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessCheck {

    public boolean canAccessProfile(String accessedById) {
        return SecurityUtil.getCurrentUserType().equals("OWNER") || SecurityUtil.isCurrentUser(accessedById);
    }

    public boolean canAccessComplaints(String accessedById) {
        return SecurityUtil.getCurrentUserType().equals("OWNER") || SecurityUtil.isCurrentUser(accessedById);
    }

    public boolean canAccessComplaint(String bookedById, String assignedToId) {
        return SecurityUtil.getCurrentUserType().equals("OWNER")
                || SecurityUtil.isCurrentUser(bookedById)
                || (assignedToId != null && SecurityUtil.isCurrentUser(assignedToId));
    }

    public boolean canAccessUpdateComplaintState(String assignedToId) {
        return SecurityUtil.getCurrentUserType().equals("OWNER")
                || (SecurityUtil.getCurrentUserType().equals("EMPLOYEE")
                    && assignedToId != null && SecurityUtil.isCurrentUser(assignedToId));
    }
}