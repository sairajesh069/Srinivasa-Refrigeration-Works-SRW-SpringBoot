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
     * Ignores userId, agreedToTerms, authorities, enabled, updatedAt, userType fields during mappings
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "agreedToTerms", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserCredential toEntity(UserCredentialDTO userCredentialDTO);

    /*
     * Converts UserCredential entity to UserCredentialDTO
     */
    UserCredentialDTO toDto(UserCredential userCredential);
}