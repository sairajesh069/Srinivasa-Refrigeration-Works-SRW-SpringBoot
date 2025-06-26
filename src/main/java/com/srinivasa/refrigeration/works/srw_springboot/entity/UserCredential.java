package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "user_credentials")
@Data
@NoArgsConstructor
public class UserCredential implements Serializable {

    @Serial
    private static final long serialVersionUID = 30L;

    @Id
    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private short enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;
}