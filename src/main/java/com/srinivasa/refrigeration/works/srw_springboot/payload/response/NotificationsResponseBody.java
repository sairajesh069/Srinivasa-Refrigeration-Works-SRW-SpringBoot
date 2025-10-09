package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationsResponseBody extends BaseResponseBody {

    private List<NotificationDTO> notifications;

    public NotificationsResponseBody(String message, int status, List<NotificationDTO> notifications) {
        super(message, status);
        this.notifications = notifications;
    }
}