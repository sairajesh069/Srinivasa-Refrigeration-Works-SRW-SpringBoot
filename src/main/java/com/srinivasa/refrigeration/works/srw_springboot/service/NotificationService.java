package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Notification;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.NotificationMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.NotificationRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.NotificationMessages;
import com.srinivasa.refrigeration.works.srw_springboot.utils.NotificationType;
import com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Cacheable(
        value = "notifications",
        key = "T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserType().equals('OWNER') " +
                "? 'notification_list' " +
                ": ('triggered_to-' + #userId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId())"
    )
    public List<NotificationDTO> notificationList(String userId) {
        if(SecurityUtil.getCurrentUserType().equals("OWNER")) {
            return notificationRepository
                    .findAllByOrderByCreatedAtDesc()
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
                saveNotification(
                        NotificationMessages.buildUnauthorizedAccessNotification(
                                "another user's notifications",
                                LocalDateTime.now()
                        )
                );
                throw new SecurityException("Unauthorized access: Attempt to fetch restricted notifications");
            }
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "notifications", allEntries = true)
    public void saveNotification(Map<String, Object> notificationData) {
        Notification notification = new Notification(
                (String) notificationData.get("userId"),
                (String) notificationData.get("title"),
                (String) notificationData.get("message"),
                (NotificationType) notificationData.get("type")
        );
        notificationRepository.save(notification);
    }
}
