package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 66L;

    private String doorNumber;
    private String street;
    private String landmark;
    private String city;
    private String district;
    private String state;
    private String pincode;
    private String country;
}
