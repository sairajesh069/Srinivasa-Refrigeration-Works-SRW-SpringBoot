package com.srinivasa.refrigeration.works.srw_springboot.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TechnicianDetails {
    private String employeeId;
    private String fullName;
    private String phoneNumber;
    private String designation;
}