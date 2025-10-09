package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_user_reference", columnNames = "employee_reference"),
                @UniqueConstraint(name = "UK_user_id", columnNames = "employee_id"),
                @UniqueConstraint(name = "UK_user_email", columnNames = "email"),
                @UniqueConstraint(name = "UK_user_phone", columnNames = "phone_number"),
                @UniqueConstraint(name = "UK_user_national_id_number", columnNames = "national_id_number")
        }
)
@Data
@NoArgsConstructor
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 50L;

    @Id
    @Column(name = "employee_reference", unique = true, nullable = false, updatable = false)
    private String employeeReference;

    @Column(name = "employee_id", unique = true, nullable = false, updatable = false)
    private String employeeId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "national_id_number", unique = true, nullable = false)
    private String nationalIdNumber;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "date_of_hire", updatable = false, nullable = false)
    private final LocalDateTime dateOfHire = LocalDateTime.now();

    @Column(name = "salary", nullable = false)
    private Long salary;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "date_of_exit")
    private LocalDateTime dateOfExit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

}