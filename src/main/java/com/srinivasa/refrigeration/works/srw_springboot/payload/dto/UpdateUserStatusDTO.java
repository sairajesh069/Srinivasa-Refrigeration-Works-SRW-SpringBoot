package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

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
public class UpdateUserStatusDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 33L;

    private String userId;
    private UserStatus userStatus;
}
