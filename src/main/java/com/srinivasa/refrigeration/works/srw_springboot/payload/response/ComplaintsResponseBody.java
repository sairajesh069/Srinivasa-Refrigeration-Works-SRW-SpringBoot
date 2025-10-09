package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintsResponseBody extends BaseResponseBody {

    private List<ComplaintDTO> complaintsDTO;

    public ComplaintsResponseBody(String message, int status, List<ComplaintDTO> complaintsDTO) {
        super(message, status);
        this.complaintsDTO = complaintsDTO;
    }
}