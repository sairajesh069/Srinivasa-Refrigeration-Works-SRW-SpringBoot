package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.NotificationResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/srw/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/list")
    public ResponseEntity<NotificationResponseBody> fetchNotifications(@RequestParam("userId") String userId) {
        try {
            List<NotificationDTO> notifications = notificationService.notificationList(userId);
            NotificationResponseBody successResponse = new NotificationResponseBody(
              "List of all notifications fetched successfully",
                    HttpStatus.OK.value(),
                    notifications
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(SecurityException exception) {
            NotificationResponseBody errorResponse = new NotificationResponseBody(
                    "Error: " + exception,
                    HttpStatus.FORBIDDEN.value(),
                    Collections.emptyList()
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        catch(Exception exception) {
            NotificationResponseBody errorResponse = new NotificationResponseBody(
                    "Error: " + exception,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Collections.emptyList()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
