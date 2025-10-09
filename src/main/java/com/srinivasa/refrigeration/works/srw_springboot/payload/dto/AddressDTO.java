package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Door number is required.")
    @Size(min = 2, message = "Door number must be at least 2 characters.")
    private String doorNumber;

    @NotBlank(message = "Street is required.")
    @Size(min = 3, message = "Street must be at least 3 characters.")
    private String street;

    private String landmark;

    @NotBlank(message = "City is required.")
    @Size(min = 3, message = "City must be at least 3 characters.")
    private String city;

    @NotBlank(message = "District is required.")
    @Size(min = 3, message = "District must be at least 3 characters.")
    private String district;

    @NotBlank(message = "State is required.")
    @Size(min = 3, message = "State must be at least 3 characters.")
    private String state;

    @NotBlank(message = "Pincode is required.")
    @Size(min = 6, message = "Pincode must be at least 6 characters.")
    private String pincode;

    @NotBlank(message = "Country is required.")
    @Size(min = 3, message = "Country must be at least 3 characters.")
    private String country;
}