package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_user_reference", columnNames = "customer_reference"),
                @UniqueConstraint(name = "UK_user_id", columnNames = "customer_id"),
                @UniqueConstraint(name = "UK_user_email", columnNames = "email"),
                @UniqueConstraint(name = "UK_user_phone", columnNames = "phone_number")
        }
)
@Data
@NoArgsConstructor
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 20L;

    @Id
    @Column(name = "customer_reference", unique = true, updatable = false, nullable = false)
    private String customerReference;

    @Column(name = "customer_id", unique = true, updatable = false, nullable = false)
    private String customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_at", updatable = false, nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

}