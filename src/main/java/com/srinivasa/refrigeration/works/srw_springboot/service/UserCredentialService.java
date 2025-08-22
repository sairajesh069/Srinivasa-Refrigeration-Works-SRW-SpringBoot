package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.UserCredential;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.UserCredentialMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AccountRecoveryDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CredentialsDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UserCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.UserCredentialRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.PhoneNumberFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveCredential(UserCredentialDTO userCredentialDTO, String userId, UserType userType) {
        UserCredential userCredential = userCredentialMapper.toEntity(userCredentialDTO);
        userCredential.setUserId(userId);
        userCredential.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(userCredential.getPhoneNumber()));
        userCredential.setUserType(userType);
        userCredential.setEnabled(userType.equals(UserType.CUSTOMER) ? (short) 1 : (short) 0);
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

    @Cacheable(value = "user-credential", key = "'fetch_user-' + #phoneNumber")
    public String fetchUsername(String phoneNumber) {
          UserCredential userCredential = userCredentialRepository
                  .findByIdentifier(PhoneNumberFormatter.formatPhoneNumber(phoneNumber))
                  .orElse(null);
          return userCredential != null ? userCredential.getUsername() : null;
    }

    @Cacheable(value = "user-credential", key = "'validate-' + #accountRecoveryDTO.loginId + '&' + #accountRecoveryDTO.phoneNumber")
    public boolean validateUser(AccountRecoveryDTO accountRecoveryDTO) {
        return userCredentialRepository.existsByLoginIdAndPhoneNumber(
                accountRecoveryDTO.getLoginId(), PhoneNumberFormatter.formatPhoneNumber(accountRecoveryDTO.getPhoneNumber()));
    }

    public void updatePassword(AccountRecoveryDTO accountRecoveryDTO) {
        userCredentialRepository.updatePassword(accountRecoveryDTO.getLoginId(), passwordEncoder.encode(accountRecoveryDTO.getPassword()));
    }
}
