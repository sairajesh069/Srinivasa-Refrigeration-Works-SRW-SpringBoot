package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Employee;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.EmployeeMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.EmployeeRepository;
import com.srinivasa.refrigeration.works.srw_springboot.repository.OwnerRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.*;
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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final OwnerRepository ownerRepository;
    private final UserCredentialService userCredentialService;

    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public EmployeeDTO addEmployee(EmployeeCredentialDTO employeeCredentialDTO) {
        Employee employee = employeeMapper.toEntity(employeeCredentialDTO.getEmployeeDTO());
        employee.setEmployeeReference(UserIdGenerator.generateUniqueId(employee.getPhoneNumber()));
        employee.setEmployeeId("SRW" + employee.getEmployeeReference() + "EMPL");
        employee.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber()));
        employee.setStatus(UserStatus.IN_ACTIVE);
        if(ownerRepository.findByIdentifier(employee.getNationalIdNumber()) != null) {
            throw new UserValidationException("Duplicate national id number");
        }
        employeeRepository.save(employee);
        userCredentialService.saveCredential(employeeCredentialDTO.getUserCredentialDTO(), employee.getEmployeeId(), UserType.EMPLOYEE);
        return employeeMapper.toDto(employee);
    }

    @Cacheable(value = "employee", key = "'fetch-' + #identifier + ', isAuthenticating-' + #isAuthenticating")
    public Object getEmployeeByIdentifier(String identifier, boolean isAuthenticating) {
        Employee employee = employeeRepository.findByIdentifier(
                identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier);
        if(isAuthenticating) {
            return new AuthenticatedUserDTO(
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    "EMPLOYEE"
            );
        }
        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "employees", allEntries = true),
                    @CacheEvict(cacheNames = "employee", key = "'fetch-' + #employeeCredentialDTO.employeeDTO.employeeId + ', isAuthenticating-' + false")
            },
            put = @CachePut(value = "employee", key = "'update-' + #employeeCredentialDTO.employeeDTO.employeeId")
    )
    public void updateEmployee(EmployeeCredentialDTO employeeCredentialDTO) {
        EmployeeDTO employeeDTO = employeeCredentialDTO.getEmployeeDTO();
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setEmployeeReference(employeeDTO.getEmployeeId().replaceAll("\\D", "").trim());
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber()));
        employee.setUpdatedAt(LocalDateTime.now());
        if(ownerRepository.findByIdentifier(employee.getNationalIdNumber()) != null) {
            throw new UserValidationException("Duplicate national id number");
        }
        if(employeeCredentialDTO.getUserCredentialDTO().getUserId() != null) {
            userCredentialService.updateDetails(employeeCredentialDTO.getUserCredentialDTO());
        }
        employeeRepository.save(employee);
    }
}
