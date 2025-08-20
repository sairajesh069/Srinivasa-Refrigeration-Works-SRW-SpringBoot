package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "owners",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_user_reference", columnNames = "owner_reference"),
                @UniqueConstraint(name = "UK_user_id", columnNames = "owner_id"),
                @UniqueConstraint(name = "UK_user_email", columnNames = "email"),
                @UniqueConstraint(name = "UK_user_phone", columnNames = "phone_number")
        }
)
@Data
@NoArgsConstructor
public class Owner implements Serializable {

    @Serial
    private static final long serialVersionUID = 40L;

    @Id
    @Column(name = "owner_reference", unique = true)
    private Long ownerReference;

    @Column(name = "owner_id", unique = true)
    private String ownerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
}