package com.srinivasa.refrigeration.works.srw_springboot.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String doorNumber;
    private String street;
    private String landmark;
    private String city;
    private String district;
    private String state;
    private String pincode;
    private String country;
}
