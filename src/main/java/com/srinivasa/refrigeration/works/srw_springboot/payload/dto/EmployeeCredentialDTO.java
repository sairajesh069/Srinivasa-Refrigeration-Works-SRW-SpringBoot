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
public class EmployeeCredentialDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 52L;

    @Valid
    private EmployeeDTO employeeDTO;

    @Valid
    private UserCredentialDTO userCredentialDTO;
}