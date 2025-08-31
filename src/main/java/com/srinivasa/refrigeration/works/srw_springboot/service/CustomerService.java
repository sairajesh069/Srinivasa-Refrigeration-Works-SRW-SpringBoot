package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Customer;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.CustomerMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.CustomerRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.PhoneNumberFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserIdGenerator;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserCredentialService userCredentialService;

    @Transactional
    @CacheEvict(cacheNames = "customers", allEntries = true)
    public CustomerDTO addCustomer(CustomerCredentialDTO customerCredentialDTO) {
        Customer customer = customerMapper.toEntity(customerCredentialDTO.getCustomerDTO());
        customer.setCustomerReference(UserIdGenerator.generateUniqueId(customer.getPhoneNumber()));
        customer.setCustomerId("SRW" + customer.getCustomerReference() + "CUST");
        customer.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(customer.getPhoneNumber()));
        customer.setStatus(UserStatus.ACTIVE);
        customerRepository.save(customer);
        userCredentialService.saveCredential(customerCredentialDTO.getUserCredentialDTO(), customer.getCustomerId(), UserType.CUSTOMER);
        return customerMapper.toDto(customer);
    }

    @Cacheable(value = "customer", key = "'fetch-' + #identifier + ', isAuthenticating-' + #isAuthenticating")
    public Object getCustomerByIdentifier(String identifier, boolean isAuthenticating) {
        Customer customer = customerRepository.findByIdentifier(
                identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier);
        if(isAuthenticating) {
            return new AuthenticatedUserDTO(
                    customer.getCustomerId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    "CUSTOMER"
            );
        }
        return customerMapper.toDto(customer);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "customers", allEntries = true),
                    @CacheEvict(cacheNames = "customer", key = "'fetch-' + #customerCredentialDTO.customerDTO.customerId + ', isAuthenticating-' + false")
            },
            put = @CachePut(value = "customer", key = "'update-' + #customerCredentialDTO.customerDTO.customerId")
    )
    public CustomerDTO updateCustomer(CustomerCredentialDTO customerCredentialDTO) {
        CustomerDTO customerDTO = customerCredentialDTO.getCustomerDTO();
        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setCustomerReference(customerDTO.getCustomerId().replaceAll("\\D", "").trim());
        customer.setCustomerId(customerDTO.getCustomerId());
        customer.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(customer.getPhoneNumber()));
        customer.setUpdatedAt(LocalDateTime.now());
        if(customerCredentialDTO.getUserCredentialDTO().getUserId() != null) {
            userCredentialService.updateDetails(customerCredentialDTO.getUserCredentialDTO());
        }
        customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = customerMapper.toDto(customer);
        updatedCustomerDTO.setCreatedAt(customerDTO.getCreatedAt());
        return updatedCustomerDTO;
    }
}