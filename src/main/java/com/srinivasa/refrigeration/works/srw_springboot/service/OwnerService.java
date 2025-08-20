package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Owner;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.OwnerMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.OwnerRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.PhoneNumberFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserIdGenerator;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {
    
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final UserCredentialService userCredentialService;

    @Transactional
    @CacheEvict(cacheNames = "owners", allEntries = true)
    public OwnerDTO addOwner(OwnerCredentialDTO ownerCredentialDTO) {
        Owner owner = ownerMapper.toEntity(ownerCredentialDTO.getOwnerDTO());
        owner.setOwnerReference(Long.parseLong(UserIdGenerator.generateUniqueId(owner.getPhoneNumber())));
        owner.setOwnerId("SRW" + owner.getOwnerReference() + "OWNR");
        owner.setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(owner.getPhoneNumber()));
        owner.setStatus(UserStatus.IN_ACTIVE);
        ownerRepository.save(owner);
        userCredentialService.saveCredential(ownerCredentialDTO.getUserCredentialDTO(), owner.getOwnerId(), UserType.OWNER);
        return ownerMapper.toDto(owner);
    }

    @Cacheable(value = "owner", key = "'fetch-' + #identifier")
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
        return ownerMapper.toDto(owner);
    }
}
