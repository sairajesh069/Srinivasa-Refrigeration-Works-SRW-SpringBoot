package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.srinivasa.refrigeration.works.srw_springboot.utils.notificationUtils.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification implements Serializable {

    @Serial
    private static final long serialVersionUID = 70L;

    @Id
    @Column(name = "notification_id", updatable = false, unique = true, nullable = false)
    private final String notificationId = UlidCreator.getUlid().toString();

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "created_at", updatable = false, nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Notification(String userId, String title, String message, NotificationType type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
    }

}