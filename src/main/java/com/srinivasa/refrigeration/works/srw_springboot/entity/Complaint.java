package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigeration.works.srw_springboot.utils.Address;
import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.TechnicianDetails;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(
        name="complaints",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_complaint_reference", columnNames = "complaint_reference"),
                @UniqueConstraint(name = "UK_complaint_id", columnNames = "complaint_id")
        }
)
@Data
@NoArgsConstructor
public class Complaint implements Serializable {

    @Serial
    private static final long serialVersionUID = 60L;

    @Id
    @Column(name="complaint_reference", unique = true)
    private String complaintReference;

    @Column(name="complaint_id", unique = true)
    private String complaintId;

    @Column(name="booked_by_id")
    private String bookedById;

    @Column(name="customer_name")
    private String customerName;

    @Column(name="contact_number")
    private String contactNumber;

    @Column(name="email")
    private String email;

    @Embedded
    private Address address;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "product_model")
    private String productModel;

    @Column(name="description")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private ComplaintStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Embedded
    private TechnicianDetails technicianDetails;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="closed_at")
    private LocalDateTime closedAt;

    @Column(name = "technician_feedback")
    private String technicianFeedback;

    @Column(name = "customer_feedback")
    private String customerFeedback;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name="reopened_at")
    private LocalDateTime reopenedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="complaintState")
    private ComplaintState complaintState;
}