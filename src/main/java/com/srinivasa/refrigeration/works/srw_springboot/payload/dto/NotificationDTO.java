package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.NotificationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 71L;

    private String notificationId;
    private String userId;
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
}