package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 62L;

    private String complaintId;
    private String bookedById;
    private String customerName;
    private String contactNumber;
    private String email;
    private AddressDTO address;
    private String productType;
    private String brand;
    private String productModel;
    private String description;
    private LocalDateTime createdAt;
    private ComplaintStatus status;
    private LocalDateTime updatedAt;
    private TechnicianDetailsDTO technicianDetails;
    private LocalDateTime closedAt;
    private String technicianFeedback;
    private String customerFeedback;
    private LocalDateTime reopenedAt;
    private ComplaintState complaintState;
}