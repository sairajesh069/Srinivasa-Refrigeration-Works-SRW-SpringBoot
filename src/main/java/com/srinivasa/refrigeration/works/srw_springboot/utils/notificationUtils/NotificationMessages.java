package com.srinivasa.refrigeration.works.srw_springboot.utils.notificationUtils;

import com.srinivasa.refrigeration.works.srw_springboot.utils.CustomDateTimeFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

public class NotificationMessages {

    public static String getUserId() {
        String UNKNOWN_USER_ID = "SRW001USER";
        try {
            String username = SecurityUtil.getCurrentUsername();
            if (username != null && !username.isBlank()) {
                return SecurityUtil.getCurrentUserId();
            }
            return UNKNOWN_USER_ID;
        } catch (SecurityException e) {
            return UNKNOWN_USER_ID;
        }
    }

    public static String buildComplaintRedirectUrl(String complaintId) {
        return (SecurityUtil.getCurrentUserType().equals("OWNER") ? "/all" : "/my")  + "-complaints?complaintId=" + complaintId;
    }

    public static String buildUserProfileRedirectUrl(String userId) {
        return "/profile?userId=" + userId;
    }

    public static String buildPhoneNumberRedirectUrl(String phoneNumber) {
        return "tel:" + phoneNumber;
    }

    public static Map<String, Object> buildWelcomeNotification(String fullName, String registeredUserId) {
        return Map.of(
                "userId", registeredUserId,
                "title", "Welcome to ServiceHub",
                "message", String.format(
                        "Hi <b>%s</b>, welcome to ServiceHub — your comprehensive service management platform. " +
                            "Your account <b>(User ID: <a href='%s'>%s</a>)</b> has been successfully created and verified. " +
                            "You can now register complaints, track service requests, and manage your profile with ease. " +
                            "If you need any assistance, our support team is always here to help.",
                        fullName, buildUserProfileRedirectUrl(registeredUserId), registeredUserId
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildUserProfileUpdatedNotification(String updatedProfileId, LocalDateTime dateTime) {
        boolean isSelfUpdate = NotificationMessages.getUserId().equals(updatedProfileId);
        String notifiedToId = NotificationMessages.getUserId().equals(updatedProfileId) ? NotificationMessages.getUserId() : updatedProfileId;
        return Map.of(
                "userId", notifiedToId,
                "title", "Profile Updated Successfully",
                "message", String.format(
                        isSelfUpdate
                                ? "Your profile <b>(User ID: <a href='%s'>%s</a>)</b> was updated successfully on <b>%s</b>. " +
                                    "The changes are now active on your account. " +
                                    "If you did not make this update, please <b><a href='%s'>contact</a></b> our support team immediately."
                                : "Your profile <b>(User ID: <a href='%s'>%s</a>)</b> has been updated by an administrator on <b>%s</b>. " +
                                    "The changes are now active on your account. " +
                                    "If this update seems incorrect, please <b><a href='%s'>contact</a></b> our support team immediately.",
                        buildUserProfileRedirectUrl(notifiedToId), notifiedToId, CustomDateTimeFormatter.formatDateTime(dateTime), buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildPasswordUpdatedNotification(String userId, LocalDateTime dateTime) {
        return Map.of(
                "userId", userId,
                "title", "Profile Password Updated Successfully",
                "message", String.format(
                        "Your profile <b>(User ID: <a href='%s'>%s</a>)</b> password has been updated successfully on <b>%s</b>. " +
                                "The changes are now active on your account. " +
                                "If you did not make this update, please <b><a href='%s'>contact</a></b> our support team immediately.",
                        buildUserProfileRedirectUrl(userId), userId, CustomDateTimeFormatter.formatDateTime(dateTime), buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildUserProfileActivatedNotification(String updatedProfileId, LocalDateTime dateTime) {
        return Map.of(
                "userId", updatedProfileId,
                "title", "Profile Activated Successfully",
                "message", String.format(
                        "Your profile <b>(User ID: <a href='%s'>%s</a>)</b> was activated by an administrator on <b>%s</b>. " +
                            "The update is now live on your account. " +
                            "If this activation seems incorrect, please <b><a href='%s'>contact</a></b> our support team immediately.",
                        buildUserProfileRedirectUrl(updatedProfileId), updatedProfileId, CustomDateTimeFormatter.formatDateTime(dateTime), buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildUserProfileDeactivatedNotification(String updatedProfileId, LocalDateTime dateTime) {
        return Map.of(
                "userId", NotificationMessages.getUserId(),
                "title", "Profile Deactivated Successfully",
                "message", String.format(
                        "Profile <b>(User ID: <a href='%s'>%s</a>)</b> was deactivated successfully on <b>%s</b>. " +
                            "The deactivation has been recorded in the system.",
                        buildUserProfileRedirectUrl(updatedProfileId), updatedProfileId, CustomDateTimeFormatter.formatDateTime(dateTime)),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildComplaintRegisteredNotification(String productType, String complaintId, LocalDateTime dateTime) {
        return Map.of(
                "userId", NotificationMessages.getUserId(),
                "title", "Service Request Approved",
                "message", String.format(
                        "Your <b>%s (Complaint ID: <a href='%s'>%s</a>)</b> repair request submitted on <b>%s</b> has been approved. " +
                            "A technician will be assigned shortly, and you will receive further updates once scheduled. " +
                            "Thank you for choosing our services.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId, CustomDateTimeFormatter.formatDateTime(dateTime)
                ),
                "type", NotificationType.INFO
                );
    }

    public static Map<String, Object> buildComplaintUpdatedNotification(String productType, String complaintId, String complaintOwnerId, LocalDateTime dateTime) {
        boolean isSelfUpdate = NotificationMessages.getUserId().equals(complaintOwnerId);
        return Map.of(
                "userId", isSelfUpdate ? NotificationMessages.getUserId() : complaintOwnerId,
                "title", "Complaint Updated Successfully",
                "message", String.format(
                        isSelfUpdate
                            ? "Your <b>%s (Complaint ID: <a href='%s'>%s</a>)</b> repair request was updated successfully on <b>%s</b>. " +
                                "The changes are now active on your account. " +
                                "If you did not make this update, please <b><a href='%s'>contact</a></b> our support team immediately."
                            : "Your <b>%s (Complaint ID: <a href='%s'>%s</a>)</b> repair request has been updated by an administrator on <b>%s</b>. " +
                                "The changes are now active on your account. " +
                                "If this update seems incorrect, please <b><a href='%s'>contact</a></b> our support team immediately.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId, CustomDateTimeFormatter.formatDateTime(dateTime), buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.INFO
                );
    }

    public static Map<String, Object> buildComplaintReopenedNotification(String productType, String complaintId, String complaintOwnerId, LocalDateTime dateTime) {
        return Map.of(
                "userId", complaintOwnerId,
                "title", "Complaint Reopened by admin/technician",
                "message", String.format(
                        "Your <b>%s (Complaint ID: <a href='%s'>%s</a>)</b> repair request has been reopened on <b>%s</b>. " +
                            "The changes are now active on your account. " +
                            "If this update seems incorrect, please <b><a href='%s'>contact</a></b> our support team immediately.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId, CustomDateTimeFormatter.formatDateTime(dateTime), buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.INFO
                );
    }

    public static Map<String, Object> buildComplaintAssignedNotification(String assignedToId, String productType, String complaintId, String customerName, String customerContact, LocalDateTime dateTime) {
        return Map.of(
                "userId", assignedToId,
                "title", "Complaint Assigned",
                "message", String.format(
                        "You have been assigned to resolve the <b>%s repair (Complaint ID: <a href='%s'>%s</a>)</b> on <b>%s</b>. " +
                            "<b>Customer: %s (Contact: <a href='%s'>%s</a>)</b>. " +
                            "Please reach out to the customer and confirm before visiting. " +
                            "Ensure the repair is completed within the scheduled time.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId, CustomDateTimeFormatter.formatDateTime(dateTime), customerName, buildPhoneNumberRedirectUrl(customerContact), customerContact
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildComplaintTransferedNotification(String oldAssigneeId, String newAssigneeId, String productType, String complaintId, String newAssigneeName, LocalDateTime dateTime) {
        return Map.of(
                "userId", oldAssigneeId,
                "title", "Assigned Complaint Transfered",
                "message", String.format(
                        "The <b>%s repair (Complaint ID: <a href='%s'>%s</a>)</b> previously assigned to you has been transferred to technician <b>%s (Technician ID: %s)</b> on <b>%s</b>. " +
                            "No further action is required from your side for this complaint." +
                            "For more details regarding this transfer, please <b><a href='%s'>contact</a></b> the administrator.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId, newAssigneeName, newAssigneeId, CustomDateTimeFormatter.formatDateTime(dateTime), buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildTechnicianAssignedNotification(String bookedById, String technicianName, String productType, String complaintId, String phoneNumber, LocalDateTime dateTime) {
        return Map.of(
                "userId", bookedById,
                "title", "Technician Assigned",
                "message", String.format(
                        "Technician <b>%s</b> is assigned on <b>%s</b> to resolve the <b>%s repair (Complaint ID: <a href='%s'>%s</a>)</b>. " +
                            "They will contact you shortly to confirm the visit. <b>Contact: <a href='%s'>%s</a></b>.  " +
                            "Please ensure someone is available at the premises during the scheduled time.",
                        technicianName, CustomDateTimeFormatter.formatDateTime(dateTime), productType, buildComplaintRedirectUrl(complaintId), complaintId, buildPhoneNumberRedirectUrl(phoneNumber), phoneNumber
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildTechnicianReAssignedNotification(String bookedById, String technicianName, String productType, String complaintId, String phoneNumber) {
        return Map.of(
                "userId", bookedById,
                "title", "Technician Re-Assigned",
                "message", String.format(
                        "Your assigned technician has been changed, and <b>%s</b> will now handle your <b>%s repair (Complaint ID: <a href='%s'>%s</a>)</b>. " +
                            "Your appointment time remains unchanged. <b>Contact: <a href='%s'>%s</a></b>. " +
                            "Please ensure someone is available at the premises during the scheduled time.",
                        technicianName, productType, buildComplaintRedirectUrl(complaintId), complaintId, buildPhoneNumberRedirectUrl(phoneNumber), phoneNumber
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildTechnicianEnRouteNotification(String bookedById, String technicianName, String productType, String complaintId, LocalDateTime dateTime, Duration period, String phoneNumber) {
        return Map.of(
                "userId", bookedById,
                "title", "Technician En-Route",
                "message", String.format(
                        "Your assigned technician <b>%s</b> is en route to your location for the scheduled <b>%s repair (Complaint ID: %s)</b>. " +
                            "Expected arrival time: <b>%s</b>. <b>Contact: <a href='%s'>%s</a></b>.  " +
                            "Please ensure someone is available at the premises during the scheduled time.",
                        technicianName, productType, complaintId,CustomDateTimeFormatter.formatEndTime(dateTime, period), buildPhoneNumberRedirectUrl(phoneNumber), phoneNumber
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildComplaintResolvedNotification(String bookedById, String productType, String complaintId, LocalDateTime dateTime, String technicianName) {
        return Map.of(
                "userId", bookedById,
                "title", "Service Completed Successfully",
                "message", String.format(
                        "Your <b>%s (Complaint ID: <a href='%s'>%s</a>)</b> repair has been completed successfully on <b>%s</b>. " +
                            "The technician <b>%s</b> has updated the complaint status and left detailed notes about the work performed. " +
                            "Please rate your service experience and provide feedback.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId, CustomDateTimeFormatter.formatDateTime(dateTime), technicianName
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildPendingFeedbackNotification(String bookedById, String complaintId, String productType) {
        return Map.of(
                "userId", bookedById,
                "title", "Reminder: Pending Service Rating",
                "message", String.format(
                        "You have a pending service rating for <b>%s repair (Complaint ID: <a href='%s'>%s</a>)</b>. " +
                            "Your feedback helps us improve our services and assists other customers in making informed decisions. " +
                            "Please take a moment to rate your recent service experience.",
                        productType, buildComplaintRedirectUrl(complaintId), complaintId
                ),
                "type", NotificationType.WARNING
        );
    }

    public static Map<String, Object> buildUnauthorizedAccessNotification(String resourceContext, LocalDateTime dateTime) {
        return Map.of(
                "userId", NotificationMessages.getUserId(),
                "title", "Unauthorized Resource Access",
                "message", String.format(
                        "We detected an attempt to access <b>%s</b> that you are not authorized to view on <b>%s</b>. " +
                            "If this was not you, please <b><a href='%s'>contact</a></b> our support team immediately.",
                        CustomDateTimeFormatter.formatDateTime(dateTime), resourceContext, buildPhoneNumberRedirectUrl("+918555976776")
                ),
                "type", NotificationType.WARNING
        );
    }

    public static Map<String, Object> buildDatabaseErrorNotification(LocalDateTime dateTime, Duration period) {
        return Map.of(
                "userId", "ALL_USERS",
                "title", "Database Connection Error",
                "message", String.format(
                        "We're experiencing temporary database connectivity issues that may affect complaint submission and status updates. " +
                            "Our technical team is actively working to resolve this issue. Expected resolution time: <b>%s</b>. " +
                            "We apologize for any inconvenience and appreciate your patience.",
                        CustomDateTimeFormatter.formatEndDateTime(dateTime, period)
                ),
                "type", NotificationType.WARNING
        );
    }

    public static Map<String, Object> buildSystemMaintenanceNotification(LocalDateTime dateTime, Duration period) {
        return Map.of(
                "userId", "ALL_USERS",
                "title", "System Maintenance Alert",
                "message", String.format(
                        "Scheduled system maintenance will occur on <b>%s</b>. " +
                            "During this time, you may experience temporary service interruptions. " +
                            "We apologize for any inconvenience and appreciate your patience.",
                        CustomDateTimeFormatter.formatWithPeriod(dateTime, period, ZoneId.of("Asia/Kolkata"))
                ),
                "type", NotificationType.WARNING
        );
    }
}