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
public class OwnerCredentialDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    private OwnerDTO ownerDTO;
    private UserCredentialDTO userCredentialDTO;
}