package com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils;

/*
 * Enum for defining complaint statuses
 * OPEN - Represents a complaint that has been registered but not yet addressed
 * IN_PROGRESS - Represents a complaint that is currently being worked on
 * RESOLVED - Represents a complaint that has been resolved
 */
public enum ComplaintStatus {
    PENDING, IN_PROGRESS, RESOLVED;
}