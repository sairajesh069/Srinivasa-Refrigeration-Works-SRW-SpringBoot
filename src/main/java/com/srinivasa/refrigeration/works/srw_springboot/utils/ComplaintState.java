package com.srinivasa.refrigeration.works.srw_springboot.utils;

/*
 * Enum representing the state of a complaint.
 * - SUBMITTED: Indicates the complaint is registered.
 * - ASSIGNED: Indicates the complaint is assigned to a technician.
 * - CLOSED: Indicates the complaint is resolved.
 * - REOPENED: Indicates the complaint is reopened with an issue.
 */
public enum ComplaintState {
    SUBMITTED, REOPENED, ASSIGNED, CLOSED;
}