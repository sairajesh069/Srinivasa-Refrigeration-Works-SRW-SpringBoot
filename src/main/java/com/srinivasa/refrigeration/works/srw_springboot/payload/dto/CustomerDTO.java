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
public class CustomerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 21L;

    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
}
