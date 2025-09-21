package com.srinivasa.refrigeration.works.srw_springboot.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

public class NotificationMessages {

    public static String userId = SecurityUtil.getCurrentUserId();

    public static Map<String, Object> buildWelcomeNotification(String fullName, String registeredUserId) {
        return Map.of(
                "userId", registeredUserId,
                "title", "Welcome to ServiceHub",
                "message", String.format(
                        "Hi %s, welcome to ServiceHub — your comprehensive service management platform. " +
                            "Your account (User Id: %s) has been successfully created and verified. " +
                            "You can now register complaints, track service requests, and manage your profile with ease. " +
                            "If you need any assistance, our support team is always here to help.",
                        fullName, registeredUserId
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildUserProfileUpdatedNotification(String updatedProfileId, LocalDateTime dateTime) {
        boolean isSelfUpdate = userId.equals(updatedProfileId);
        return Map.of(
                "userId", isSelfUpdate ? userId : updatedProfileId,
                "title", "Profile Updated Successfully",
                "message", String.format(
                        isSelfUpdate
                                ? "Your profile (User ID: %s) was updated successfully on %s. " +
                                    "The changes are now active on your account. " +
                                    "If you did not make this update, please contact our support team immediately."
                                : "Your profile (User ID: %s) has been updated by an administrator on %s. " +
                                    "The changes are now active on your account. " +
                                    "If this update seems incorrect, please contact our support team immediately.",
                        isSelfUpdate ? userId : updatedProfileId, CustomDateTimeFormatter.formatDateTime(dateTime)),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildUserProfileActivatedNotification(String updatedProfileId, LocalDateTime dateTime) {
        return Map.of(
                "userId", updatedProfileId,
                "title", "Profile Activated Successfully",
                "message", String.format(
                        "Your profile (User ID: %s) was activated by an administrator on %s. " +
                            "The update is now live on your account. " +
                            "If this activation seems incorrect, please contact our support team immediately.",
                        updatedProfileId, CustomDateTimeFormatter.formatDateTime(dateTime)),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildUserProfileDeactivatedNotification(String updatedProfileId, LocalDateTime dateTime) {
        return Map.of(
                "userId", userId,
                "title", "Profile Deactivated Successfully",
                "message", String.format(
                        "Profile (User ID: %s) was deactivated by Admin (Admin ID: %s) on %s. " +
                            "The deactivation has been recorded in the system.",
                        updatedProfileId, userId, CustomDateTimeFormatter.formatDateTime(dateTime)),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildComplaintRegisteredNotification(String productType, String complaintId, LocalDateTime dateTime) {
        return Map.of(
                "userId", userId,
                "title", "Service Request Approved",
                "message", String.format(
                        "Your %s (Complaint Id: %s) repair request submitted on %s has been approved. " +
                            "A technician will be assigned shortly, and you will receive further updates once scheduled. " +
                            "Thank you for choosing our services.",
                        productType, complaintId, CustomDateTimeFormatter.formatDateTime(dateTime)
                ),
                "type", NotificationType.INFO
                );
    }

    public static Map<String, Object> buildComplaintUpdatedNotification(String productType, String complaintId, String complaintOwnerId, LocalDateTime dateTime) {
        boolean isSelfUpdate = userId.equals(complaintOwnerId);
        return Map.of(
                "userId", isSelfUpdate ? userId : complaintOwnerId,
                "title", "Complaint Updated Successfully",
                "message", String.format(
                        isSelfUpdate
                            ? "Your %s (Complaint Id: %s) repair request was updated successfully on %s. " +
                                "The changes are now active on your account. " +
                                "If you did not make this update, please contact our support team immediately."
                            : "Your %s (Complaint Id: %s) repair request has been updated by an administrator on %s. " +
                                "The changes are now active on your account. " +
                                "If this update seems incorrect, please contact our support team immediately.",
                        productType, complaintId, CustomDateTimeFormatter.formatDateTime(dateTime)
                ),
                "type", NotificationType.INFO
                );
    }

    public static Map<String, Object> buildComplaintReopenedNotification(String productType, String complaintId, String complaintOwnerId, LocalDateTime dateTime) {
        return Map.of(
                "userId", complaintOwnerId,
                "title", "Complaint Reopened by admin/technician",
                "message", String.format(
                        "Your %s (Complaint Id: %s) repair request has been reopened on %s. " +
                            "The changes are now active on your account. " +
                            "If this update seems incorrect, please contact our support team immediately.",
                        productType, complaintId, CustomDateTimeFormatter.formatDateTime(dateTime)
                ),
                "type", NotificationType.INFO
                );
    }

    public static Map<String, Object> buildComplaintAssignedNotification(String assignedToId, String productType, String complaintId, String customerName, String customerContact, LocalDateTime dateTime) {
        return Map.of(
                "userId", assignedToId,
                "title", "Complaint Assigned",
                "message", String.format(
                        "You have been assigned to resolve the %s repair (Complaint ID: %s) on %s. " +
                            "Customer: %s (Contact: %s). " +
                            "Please reach out to the customer and confirm before visiting. " +
                            "Ensure the repair is completed within the scheduled time.",
                        productType, complaintId, CustomDateTimeFormatter.formatDateTime(dateTime), customerName, customerContact
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildComplaintTransferedNotification(String oldAssigneeId, String newAssigneeId, String productType, String complaintId, String newAssigneeName, LocalDateTime dateTime) {
        return Map.of(
                "userId", oldAssigneeId,
                "title", "Assigned Complaint Transfered",
                "message", String.format(
                        "The %s repair (Complaint ID: %s) previously assigned to you has been transferred to technician %s (Technician ID: %s) on %s. " +
                            "No further action is required from your side for this complaint." +
                            "For more details regarding this transfer, please contact the administrator.",
                        productType, complaintId, newAssigneeName, newAssigneeId, CustomDateTimeFormatter.formatDateTime(dateTime)
                ),
                "type", NotificationType.INFO
        );
    }

    public static Map<String, Object> buildTechnicianAssignedNotification(String bookedById, String technicianName, String productType, String complaintId, String phoneNumber, LocalDateTime dateTime) {
        return Map.of(
                "userId", bookedById,
                "title", "Technician Assigned",
                "message", String.format(
                        "Technician %s is assigned on %s to resolve the %s (Complaint Id: %s) repair. " +
                            "They will contact you shortly to confirm the visit. Contact: %s.  " +
                            "Please ensure someone is available at the premises during the scheduled time.",
                        technicianName, CustomDateTimeFormatter.formatDateTime(dateTime), productType, complaintId, phoneNumber
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildTechnicianReAssignedNotification(String bookedById, String technicianName, String productType, String complaintId, String phoneNumber) {
        return Map.of(
                "userId", bookedById,
                "title", "Technician Re-Assigned",
                "message", String.format(
                        "Your assigned technician has been changed, and %s will now handle your %s repair (Complaint ID: %s). " +
                            "Your appointment time remains unchanged. Contact: %s. " +
                            "Please ensure someone is available at the premises during the scheduled time.",
                        technicianName, productType, complaintId, phoneNumber
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildTechnicianEnRouteNotification(String bookedById, String technicianName, String productType, String complaintId, LocalDateTime dateTime, Duration period, String phoneNumber) {
        return Map.of(
                "userId", bookedById,
                "title", "Technician En-Route",
                "message", String.format(
                        "Your assigned technician %s is en route to your location for the scheduled %s (Complaint Id: %s) repair. " +
                            "Expected arrival time: %s. Contact: %s.  " +
                            "Please ensure someone is available at the premises during the scheduled time.",
                        technicianName, productType, complaintId,CustomDateTimeFormatter.formatEndTime(dateTime, period), phoneNumber
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildComplaintResolvedNotification(String bookedById, String productType, String complaintId, LocalDateTime dateTime, String technicianName) {
        return Map.of(
                "userId", bookedById,
                "title", "Service Completed Successfully",
                "message", String.format(
                        "Your %s (Complaint Id: %s) repair has been completed successfully on %s. " +
                            "The technician %s has updated the complaint status and left detailed notes about the work performed. " +
                            "Please rate your service experience and provide feedback.",
                        productType, complaintId, CustomDateTimeFormatter.formatDateTime(dateTime), technicianName
                ),
                "type", NotificationType.REPAIR_UPDATE
        );
    }

    public static Map<String, Object> buildPendingFeedbackNotification(String bookedById, String complaintId) {
        return Map.of(
                "userId", bookedById,
                "title", "Reminder: Pending Service Rating",
                "message", String.format(
                        "You have a pending service rating for complaint %s. " +
                            "Your feedback helps us improve our services and assists other customers in making informed decisions. " +
                            "Please take a moment to rate your recent service experience.",
                        complaintId
                ),
                "type", NotificationType.WARNING
        );
    }

    public static Map<String, Object> buildUnauthorizedAccessNotification(String resourceContext, LocalDateTime dateTime) {
        return Map.of(
                "userId", userId,
                "title", "Unauthorized Resource Access",
                "message", String.format(
                        "We detected an attempt to access %s that you are not authorized to view on %s. " +
                            "If this was not you, please contact our support team immediately.",
                        CustomDateTimeFormatter.formatDateTime(dateTime), resourceContext
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
                            "Our technical team is actively working to resolve this issue. Expected resolution time: %s. " +
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
                        "Scheduled system maintenance will occur on %s. " +
                            "During this time, you may experience temporary service interruptions. " +
                            "We apologize for any inconvenience and appreciate your patience.",
                        CustomDateTimeFormatter.formatWithPeriod(dateTime, period, ZoneId.of("Asia/Kolkata"))
                ),
                "type", NotificationType.WARNING
        );
    }
}
