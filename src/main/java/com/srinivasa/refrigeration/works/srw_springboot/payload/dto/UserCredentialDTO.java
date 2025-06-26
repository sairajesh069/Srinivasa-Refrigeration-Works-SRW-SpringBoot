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
public class UserCredentialDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 31L;

    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}