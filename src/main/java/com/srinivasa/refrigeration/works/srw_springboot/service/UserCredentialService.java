package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.UserCredential;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.UserCredentialMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UserCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.UserCredentialRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.PhoneNumberFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
    }
}
