package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Customer;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.CustomerMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateUserStatusDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.CustomerRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserCredentialService userCredentialService;
    private final AccessCheck accessCheck;

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

    @Cacheable(value = "customer",
            key = "#isAuthenticating ? " +
                    "'fetch-' + #identifier + '-isAuthenticating-' + #isAuthenticating : " +
                    "'fetch-' + #identifier + '-isAuthenticating-' + #isAuthenticating + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
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
        else {
            if (accessCheck.canAccessProfile(customer.getCustomerId())) {
                return customerMapper.toDto(customer);
            } else {
                throw new SecurityException("Unauthorized access: Attempt to fetch restricted customer profile");
            }
        }
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "customers", allEntries = true),
                    @CacheEvict(cacheNames = "customer", key = "'fetch-' + #customerCredentialDTO.customerDTO.customerId + '-isAuthenticating-' + true"),
                    @CacheEvict(cacheNames = "customer", key = "'fetch-' + #customerCredentialDTO.customerDTO.customerId + '-isAuthenticating-' + false + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
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

    @Cacheable(value = "customers", key = "'customer_list'")
    public List<CustomerDTO> getCustomerList() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "customers", allEntries = true),
                    @CacheEvict(cacheNames = "customer", key = "'fetch-' + #updateUserStatusDTO.userId + '-isAuthenticating-' + true"),
                    @CacheEvict(cacheNames = "customer", key = "'fetch-' + #updateUserStatusDTO.userId + '-isAuthenticating-' + false + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
            },
            put = @CachePut(value = "customer", key = "#updateUserStatusDTO.userStatus + '-' + #updateUserStatusDTO.userId")
    )
    public void updateStatus(UpdateUserStatusDTO updateUserStatusDTO) {
        String userId = updateUserStatusDTO.getUserId();
        UserStatus status = updateUserStatusDTO.getUserStatus();
        int enabled = status.equals(UserStatus.ACTIVE) ? 1 : 0;
        customerRepository.updateStatusById(userId, status, LocalDateTime.now());
        userCredentialService.updateUserStatus(userId, (byte) enabled);
    }
}