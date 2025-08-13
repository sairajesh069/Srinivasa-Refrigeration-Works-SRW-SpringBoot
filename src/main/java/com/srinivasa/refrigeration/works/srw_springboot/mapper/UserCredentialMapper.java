package com.srinivasa.refrigeration.works.srw_springboot.mapper;

import com.srinivasa.refrigeration.works.srw_springboot.entity.UserCredential;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UserCredentialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Mapper interface for converting between UserCredential entity and UserCredentialDTO
 */
@Mapper(componentModel = "spring")
public interface UserCredentialMapper {

    /*
     * Converts UserCredentialDTO to UserCredential entity
     * Ignores userId, phoneNumber, enabled, userType fields during mappings
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "userType", ignore = true)
    UserCredential toEntity(UserCredentialDTO userCredentialDTO);

    /*
     * Converts UserCredential entity to UserCredentialDTO
     */
    UserCredentialDTO toDto(UserCredential userCredential);
}