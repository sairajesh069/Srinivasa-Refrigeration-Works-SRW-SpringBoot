package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationResponseBody {

    public NotificationResponseBody(String message, int status, List<NotificationDTO> notifications) {
        this.message = message;
        this.status = status;
        this.notifications = notifications;
    }

    private String message;
    private int status;
    private List<NotificationDTO> notifications;
    private LocalDateTime timestamp = LocalDateTime.now();
}