package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Owner;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.OwnerMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateUserStatusDTO;
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
public class OwnerService {
    
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final EmployeeRepository employeeRepository;
    private final UserCredentialService userCredentialService;
    private final OtpService otpService;
    private final AccessCheck accessCheck;
    private final NotificationService notificationService;

    @Transactional
    @CacheEvict(cacheNames = "owners", allEntries = true)
    public OwnerDTO addOwner(OwnerCredentialDTO ownerCredentialDTO) {

        boolean isOtpRequired = accessCheck.isOtpVerificationRequired();

        String phoneNumber = ownerCredentialDTO.getUserCredentialDTO().getPhoneNumber();
        FieldConsistencyValidator.validate(ownerCredentialDTO.getOwnerDTO().getPhoneNumber(), phoneNumber, "Phone Number");
        if(isOtpRequired) {
            otpService.validateOtp(phoneNumber, ownerCredentialDTO.getOwnerDTO().getPhoneNumberOtp(), "phone number");
        }

        String email = ownerCredentialDTO.getUserCredentialDTO().getEmail();
        FieldConsistencyValidator.validate(ownerCredentialDTO.getOwnerDTO().getEmail(), email, "Email");
        if(isOtpRequired) {
            otpService.validateOtp(email, ownerCredentialDTO.getOwnerDTO().getEmailOtp(), "email");
        }

        Owner owner = ownerMapper.toEntity(ownerCredentialDTO.getOwnerDTO());
        owner.setOwnerReference(UserIdGenerator.generateUniqueId(owner.getPhoneNumber()));
        owner.setOwnerId("SRW" + owner.getOwnerReference() + "OWNR");
        owner.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(owner.getPhoneNumber()));
        owner.setStatus(UserStatus.IN_ACTIVE);

        if(employeeRepository.findByIdentifier(owner.getNationalIdNumber()) != null) {
            throw new IllegalArgumentException("Duplicate national id number");
        }

        ownerRepository.save(owner);
        userCredentialService.saveCredential(ownerCredentialDTO.getUserCredentialDTO(), owner.getOwnerId(), UserType.OWNER);

        notificationService.saveNotification(
                NotificationMessages.buildWelcomeNotification(
                        owner.getFirstName() + " " + owner.getLastName(),
                        owner.getOwnerId()
                )
        );

        return ownerMapper.toDto(owner);
    }

    @Cacheable(value = "owner",
            key = "#isAuthenticating ? " +
                    "'fetch-' + #identifier + '-isAuthenticating-' + #isAuthenticating : " +
                    "'fetch-' + #identifier + '-isAuthenticating-' + #isAuthenticating + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil).getCurrentUserId()")
    public Object getOwnerByIdentifier(String identifier, boolean isAuthenticating) {

        Owner owner = ownerRepository.findByIdentifier(
                identifier.matches("\\d{10}") ? PhoneNumberFormatter.formatPhoneNumber(identifier) : identifier);

        if(isAuthenticating) {
            return new AuthenticatedUserDTO(
                    owner.getOwnerId(),
                    owner.getFirstName(),
                    owner.getLastName(),
                    "OWNER"
            );
        }
        else {
            if (accessCheck.canAccessProfile(owner.getOwnerId())) {
                return ownerMapper.toDto(owner);
            }
            else {
                notificationService.saveNotification(
                        NotificationMessages.buildUnauthorizedAccessNotification(
                                "another owner profile",
                                LocalDateTime.now()
                        )
                );
                throw new SecurityException("Unauthorized access: Attempt to fetch restricted owner profile");
            }
        }
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "owners", allEntries = true),
                    @CacheEvict(cacheNames = "owner", key = "'fetch-' + #ownerCredentialDTO.ownerDTO.ownerId + '-isAuthenticating-' + true"),
                    @CacheEvict(cacheNames = "owner", key = "'fetch-' + #ownerCredentialDTO.ownerDTO.ownerId + '-isAuthenticating-' + false + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil).getCurrentUserId()")
            },
            put = @CachePut(value = "owner", key = "'update-' + #ownerCredentialDTO.ownerDTO.ownerId")
    )
    public OwnerDTO updateOwner(OwnerCredentialDTO ownerCredentialDTO) {

        OwnerDTO ownerDTO = ownerCredentialDTO.getOwnerDTO();

        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner.setOwnerReference(ownerDTO.getOwnerId().replaceAll("\\D", "").trim());
        owner.setOwnerId(ownerDTO.getOwnerId());
        owner.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(owner.getPhoneNumber()));
        owner.setUpdatedAt(LocalDateTime.now());

        if(employeeRepository.findByIdentifier(owner.getNationalIdNumber()) != null) {
            throw new IllegalArgumentException("Duplicate national id number");
        }

        if(ownerCredentialDTO.getUserCredentialDTO().getUserId() != null) {

            boolean isOtpRequired = accessCheck.isOtpVerificationRequired();

            String phoneNumber = ownerCredentialDTO.getUserCredentialDTO().getPhoneNumber();
            if(phoneNumber != null) {
                FieldConsistencyValidator.validate(ownerDTO.getPhoneNumber(), phoneNumber, "Phone number");
                if(isOtpRequired) {
                    otpService.validateOtp(phoneNumber, ownerCredentialDTO.getOwnerDTO().getPhoneNumberOtp(), "phone number");
                }
            }

            String email = ownerCredentialDTO.getUserCredentialDTO().getEmail();
            if(email != null) {
                FieldConsistencyValidator.validate(ownerDTO.getEmail(), email, "Email");
                if(isOtpRequired) {
                    otpService.validateOtp(email, ownerCredentialDTO.getOwnerDTO().getEmailOtp(), "email");
                }
            }

            userCredentialService.updateDetails(ownerCredentialDTO.getUserCredentialDTO());
        }

        ownerRepository.save(owner);

        notificationService.saveNotification(
                NotificationMessages.buildUserProfileUpdatedNotification(
                        owner.getOwnerId(),
                        LocalDateTime.now()
                )
        );

        OwnerDTO updatedOwnerDTO = ownerMapper.toDto(owner);
        updatedOwnerDTO.setCreatedAt(ownerDTO.getCreatedAt());
        return updatedOwnerDTO;
    }

    @Cacheable(value = "owners", key = "'owner_list'")
    public List<OwnerDTO> getOwnerList() {
        return ownerRepository
                .findAll()
                .stream()
                .map(ownerMapper::toDto)
                .toList();
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "owners", allEntries = true),
                    @CacheEvict(cacheNames = "owner", key = "'fetch-' + #updateUserStatusDTO.userId + '-isAuthenticating-' + true"),
                    @CacheEvict(cacheNames = "owner", key = "'fetch-' + #updateUserStatusDTO.userId + '-isAuthenticating-' + false + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.SecurityUtil).getCurrentUserId()")
            }
    )
    public void updateStatus(UpdateUserStatusDTO updateUserStatusDTO) {

        String userId = updateUserStatusDTO.getUserId();
        UserStatus status = updateUserStatusDTO.getUserStatus();
        int enabled = status.equals(UserStatus.ACTIVE) ? 1 : 0;

        ownerRepository.updateStatusById(userId, status, LocalDateTime.now());
        userCredentialService.updateUserStatus(userId, (byte) enabled);

        notificationService.saveNotification(status.equals(UserStatus.ACTIVE)
                ? NotificationMessages.buildUserProfileActivatedNotification(userId, LocalDateTime.now())
                : NotificationMessages.buildUserProfileDeactivatedNotification(userId, LocalDateTime.now())
        );
    }
}