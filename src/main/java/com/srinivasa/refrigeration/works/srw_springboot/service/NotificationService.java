package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.mapper.NotificationMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.NotificationRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationDTO> notificationList(String userId) {
        if(SecurityUtil.getCurrentUserType().equals("OWNER")) {
            return notificationRepository
                    .findAll()
                    .stream()
                    .map(notificationMapper::toDto)
                    .toList();
        }
        else {
            if(SecurityUtil.isCurrentUser(userId)) {
                return notificationRepository
                        .findAllByUserId(userId)
                        .stream()
                        .map(notificationMapper::toDto)
                        .toList();
            }
            else {
                throw new SecurityException("Unauthorized access: Attempt to fetch restricted notifications");
            }
        }
    }
}
