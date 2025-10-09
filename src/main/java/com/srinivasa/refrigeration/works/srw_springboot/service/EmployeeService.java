package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Employee;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.EmployeeMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.*;
import com.srinivasa.refrigeration.works.srw_springboot.repository.EmployeeRepository;
import com.srinivasa.refrigeration.works.srw_springboot.repository.OwnerRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.*;
import com.srinivasa.refrigeration.works.srw_springboot.utils.notificationUtils.NotificationMessages;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserIdGenerator;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserType;
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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final OwnerRepository ownerRepository;
    private final UserCredentialService userCredentialService;
    private final AccessCheck accessCheck;
    private final NotificationService notificationService;

    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public EmployeeDTO addEmployee(EmployeeCredentialDTO employeeCredentialDTO) {

        Employee employee = employeeMapper.toEntity(employeeCredentialDTO.getEmployeeDTO());
        employee.setEmployeeReference(UserIdGenerator.generateUniqueId(employee.getPhoneNumber()));
        employee.setEmployeeId("SRW" + employee.getEmployeeReference() + "EMPL");
        employee.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber()));
        employee.setStatus(UserStatus.IN_ACTIVE);

        if(ownerRepository.findByIdentifier(employee.getNationalIdNumber()) != null) {
            throw new IllegalArgumentException("Duplicate national id number");
        }

        employeeRepository.save(employee);
        userCredentialService.saveCredential(employeeCredentialDTO.getUserCredentialDTO(), employee.getEmployeeId(), UserType.EMPLOYEE);

        notificationService.saveNotification(
                NotificationMessages.buildWelcomeNotification(
                        employee.getFirstName() + " " + employee.getLastName(),
                        employee.getEmployeeId()
                )
        );

        return employeeMapper.toDto(employee);
    }

    @Cacheable(value = "employee",
            key = "#isAuthenticating ? " +
                    "'fetch-' + #identifier + '-isAuthenticating-' + #isAuthenticating : " +
                    "'fetch-' + #identifier + '-isAuthenticating-' + #isAuthenticating + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil).getCurrentUserId()")
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
        else {
            if (accessCheck.canAccessProfile(employee.getEmployeeId())) {
                return employeeMapper.toDto(employee);
            }
            else {
                notificationService.saveNotification(
                        NotificationMessages.buildUnauthorizedAccessNotification(
                                "another employee profile",
                                LocalDateTime.now()
                        )
                );
                throw new SecurityException("Unauthorized access: Attempt to fetch restricted employee profile");
            }
        }
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "employees", allEntries = true),
                    @CacheEvict(cacheNames = "employee", key = "'fetch-' + #employeeCredentialDTO.employeeDTO.employeeId + '-isAuthenticating-' + true"),
                    @CacheEvict(cacheNames = "employee", key = "'fetch-' + #employeeCredentialDTO.employeeDTO.employeeId + '-isAuthenticating-' + false + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil).getCurrentUserId()")
            },
            put = @CachePut(value = "employee", key = "'update-' + #employeeCredentialDTO.employeeDTO.employeeId")
    )
    public EmployeeDTO updateEmployee(EmployeeCredentialDTO employeeCredentialDTO) {

        EmployeeDTO employeeDTO = employeeCredentialDTO.getEmployeeDTO();

        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setEmployeeReference(employeeDTO.getEmployeeId().replaceAll("\\D", "").trim());
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(employee.getPhoneNumber()));
        employee.setUpdatedAt(LocalDateTime.now());

        if(ownerRepository.findByIdentifier(employee.getNationalIdNumber()) != null) {
            throw new IllegalArgumentException("Duplicate national id number");
        }

        if(employeeCredentialDTO.getUserCredentialDTO().getUserId() != null) {
            userCredentialService.updateDetails(employeeCredentialDTO.getUserCredentialDTO());
        }

        employeeRepository.save(employee);

        notificationService.saveNotification(
                NotificationMessages.buildUserProfileUpdatedNotification(
                        employee.getEmployeeId(),
                        LocalDateTime.now()
                )
        );

        EmployeeDTO updatedEmployeeDTO = employeeMapper.toDto(employee);
        updatedEmployeeDTO.setDateOfHire(employeeDTO.getDateOfHire());
        return updatedEmployeeDTO;
    }

    @Cacheable(value = "employees", key = "'active_employees-' + #context")
    public List<?> getActiveEmployeeList(String context) {

        List<Employee> employees = employeeRepository.findByStatus(UserStatus.ACTIVE);

        if(context.equals("update-complaint")) {
            return employees
                .stream()
                .map(employee ->
                        new TechnicianDetailsDTO(
                                employee.getEmployeeId(),
                                employee.getFirstName() + " " + employee.getLastName(),
                                employee.getPhoneNumber(),
                                employee.getDesignation()
                        )
                )
                .toList();
        }
        else if(context.equals("all-employees")) {
            return employees
                    .stream()
                    .map(employeeMapper::toDto)
                    .toList();
        }
        return List.of();
    }

    @Cacheable(value = "employees", key = "'employee_list'")
    public List<EmployeeDTO> getEmployeeList() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "employees", allEntries = true),
                    @CacheEvict(cacheNames = "employee", key = "'fetch-' + #updateUserStatusDTO.userId + '-isAuthenticating-' + true"),
                    @CacheEvict(cacheNames = "employee", key = "'fetch-' + #updateUserStatusDTO.userId + '-isAuthenticating-' + false + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil).getCurrentUserId()")
            }
    )
    public void updateStatus(UpdateUserStatusDTO updateUserStatusDTO) {

        String userId = updateUserStatusDTO.getUserId();
        UserStatus status = updateUserStatusDTO.getUserStatus();
        int enabled = status.equals(UserStatus.ACTIVE) ? 1 : 0;

        employeeRepository.updateStatusById(userId, status, LocalDateTime.now());
        userCredentialService.updateUserStatus(userId, (byte) enabled);

        notificationService.saveNotification(status.equals(UserStatus.ACTIVE)
                ? NotificationMessages.buildUserProfileActivatedNotification(userId, LocalDateTime.now())
                : NotificationMessages.buildUserProfileDeactivatedNotification(userId, LocalDateTime.now())
        );
    }
}