package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import jakarta.validation.Valid;
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
public class CustomerCredentialDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 22L;

    @Valid
    private CustomerDTO customerDTO;

    @Valid
    private UserCredentialDTO userCredentialDTO;
}