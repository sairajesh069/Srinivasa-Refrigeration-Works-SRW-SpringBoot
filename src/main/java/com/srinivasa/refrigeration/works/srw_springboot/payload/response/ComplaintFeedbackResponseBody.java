package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintFeedbackDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintFeedbackResponseBody extends BaseResponseBody {

    private ComplaintFeedbackDTO complaintFeedbackDTO;

    public ComplaintFeedbackResponseBody(String message, int status, ComplaintFeedbackDTO complaintFeedbackDTO) {
        super(message, status);
        this.complaintFeedbackDTO = complaintFeedbackDTO;
    }
}