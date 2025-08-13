package com.srinivasa.refrigeration.works.srw_springboot.mapper;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Customer;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Mapper interface for converting between Customer entity and CustomerDTO
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /*
     * Converts CustomerDTO to Customer entity
     * (ignores customerReference, createdAt, updatedAt, customerId fields)
     */
    @Mapping(target = "customerReference", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);

    /*
     * Converts Customer entity to CustomerDTO
     */
    CustomerDTO toDto(Customer customer);
}