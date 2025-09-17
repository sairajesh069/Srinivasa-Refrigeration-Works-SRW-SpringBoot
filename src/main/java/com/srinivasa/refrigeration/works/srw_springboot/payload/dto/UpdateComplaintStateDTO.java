package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComplaintStateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 68L;

    private String complaintId;
    private ComplaintState complaintState;
    private String assignedTo;
}
