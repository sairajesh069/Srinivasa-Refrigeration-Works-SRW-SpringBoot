package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

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
public class AuthenticatedUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 11L;

    private String userId;
    private String firstName;
    private String lastName;
    private String userType;
}
