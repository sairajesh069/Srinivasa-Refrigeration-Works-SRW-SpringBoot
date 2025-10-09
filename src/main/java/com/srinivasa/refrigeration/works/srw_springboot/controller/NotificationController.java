package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.NotificationsResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.NotificationService;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/srw/notification")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/list")
    public ResponseEntity<NotificationsResponseBody> fetchNotifications(@RequestParam("userId") @ValidUserId String userId) {

        List<NotificationDTO> notifications = notificationService.notificationList(userId);

        NotificationsResponseBody successResponse = new NotificationsResponseBody(
                "List of all notifications fetched successfully",
                HttpStatus.OK.value(),
                notifications
        );
        return ResponseEntity.ok(successResponse);
    }
}