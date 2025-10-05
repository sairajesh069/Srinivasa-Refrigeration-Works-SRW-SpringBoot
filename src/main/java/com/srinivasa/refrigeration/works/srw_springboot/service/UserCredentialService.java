package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.UserCredential;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.UserCredentialMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AccountRecoveryDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ChangePasswordDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CredentialsDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UserCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.UserCredentialRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.NotificationMessages;
import com.srinivasa.refrigeration.works.srw_springboot.utils.PhoneNumberFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    @Transactional
    @CacheEvict(cacheNames = "user-credential", allEntries = true)
    public void saveCredential(UserCredentialDTO userCredentialDTO, String userId, UserType userType) {
        if(userCredentialDTO.getAgreedToTerms() != 1) {
            throw new IllegalArgumentException("Please accept the Terms and Conditions to complete registration.");
        }
        UserCredential userCredential = userCredentialMapper.toEntity(userCredentialDTO);
        userCredential.setUserId(userId);
        userCredential.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(userCredential.getPhoneNumber()));
        userCredential.setUserType(userType);
        userCredential.setEnabled(userType.equals(UserType.CUSTOMER) ? (short) 1 : (short) 0);
        userCredential.setAgreedToTerms(userCredentialDTO.getAgreedToTerms());
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
    }

    public Map<String, String> userValidationAndGetUserId(CredentialsDTO credentialsDTO) {
        UserCredential userCredential = userCredentialRepository.findByLoginId(credentialsDTO.getLoginId().toLowerCase());
        if(userCredential == null) {
            throw new UserValidationException("Invalid login id");
        }

        if(userCredential.getEnabled() == 0) {
            throw new UserValidationException("Inactive user");
        }

        if(!passwordEncoder.matches(credentialsDTO.getPassword(), userCredential.getPassword())) {
            throw new UserValidationException("Invalid password");
        }

        return Map.of(
            "username", userCredential.getUsername(),
            "userId", userCredential.getUserId(),
            "userType", userCredential.getUserType().name()
        );
    }

    @Cacheable(value = "user-credential", key = "'fetch_user-' + #identifier")
    public String fetchUsername(String identifier) {
          UserCredential userCredential = userCredentialRepository
                  .findByIdentifier(identifier.matches("^[0-9]{10}$")
                          ? PhoneNumberFormatter.formatPhoneNumber(identifier)
                          : identifier)
                  .orElse(null);
          return userCredential != null ? userCredential.getUsername() : null;
    }

    @Cacheable(value = "user-credential", key = "'validate-' + #accountRecoveryDTO.loginId + '&' + #accountRecoveryDTO.phoneNumber")
    public boolean validateUser(AccountRecoveryDTO accountRecoveryDTO) {
        return userCredentialRepository.existsByLoginIdAndPhoneNumber(
                accountRecoveryDTO.getLoginId(), PhoneNumberFormatter.formatPhoneNumber(accountRecoveryDTO.getPhoneNumber()));
    }

    @CacheEvict(cacheNames = "user-credential", allEntries = true)
    public void updatePassword(Object DTO, boolean isAuthenticated) {
        if(isAuthenticated) {
            ChangePasswordDTO changePasswordDTO = (ChangePasswordDTO) DTO;
            UserCredential userCredential = userCredentialRepository
                    .findByIdentifier(changePasswordDTO.getUsername())
                    .orElse(null);
            if(userCredential != null && !passwordEncoder.matches(changePasswordDTO.getOldPassword(), userCredential.getPassword())) {
                throw new UserValidationException("Invalid current password");
            }
            userCredentialRepository.updatePassword(changePasswordDTO.getUsername(),
                    passwordEncoder.encode(changePasswordDTO.getNewPassword()), LocalDateTime.now());
            notificationService.saveNotification(
                    NotificationMessages.buildPasswordUpdatedNotification(
                            userCredential != null ? userCredential.getUserId() : "",
                            LocalDateTime.now()
                    )
            );
        }
        else {
            AccountRecoveryDTO accountRecoveryDTO = (AccountRecoveryDTO) DTO;
            UserCredential userCredential = userCredentialRepository
                    .findByIdentifier(accountRecoveryDTO.getLoginId())
                    .orElse(null);
            if(userCredential != null && passwordEncoder.matches(accountRecoveryDTO.getPassword(), userCredential.getPassword())) {
                throw new UserValidationException("New password must be different from the old password.");
            }
            else {
                userCredentialRepository.updatePassword(accountRecoveryDTO.getLoginId(),
                        passwordEncoder.encode(accountRecoveryDTO.getPassword()), LocalDateTime.now()
                );
                notificationService.saveNotification(
                        NotificationMessages.buildPasswordUpdatedNotification(
                                userCredential != null ? userCredential.getUserId() : "",
                                LocalDateTime.now()
                        )
                );
            }
        }
    }

    @CacheEvict(cacheNames = "user-credential", allEntries = true)
    public void updateDetails(UserCredentialDTO userCredentialDTO) {
        userCredentialRepository.updateDetails(userCredentialDTO.getUserId(),
                PhoneNumberFormatter.formatPhoneNumber(userCredentialDTO.getPhoneNumber()),
                userCredentialDTO.getEmail());
    }

    @CacheEvict(cacheNames = "user-credential", allEntries = true)
    public void updateUserStatus(String userId, byte enabled) {
        userCredentialRepository.updateUserStatusById(userId, enabled, LocalDateTime.now());
    }
}
