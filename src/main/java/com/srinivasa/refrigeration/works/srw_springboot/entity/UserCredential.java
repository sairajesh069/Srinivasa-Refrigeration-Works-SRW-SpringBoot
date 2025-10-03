package com.srinivasa.refrigeration.works.srw_springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(
        name = "user_credentials",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_user_id", columnNames = "user_id"),
                @UniqueConstraint(name = "UK_username", columnNames = "username"),
                @UniqueConstraint(name = "UK_user_email", columnNames = "email"),
                @UniqueConstraint(name = "UK_user_phone", columnNames = "phone_number")
        }
)
@Data
@NoArgsConstructor
public class UserCredential implements UserDetails, Serializable {

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

    @Column(name = "agreed_to_terms")
    private short agreedToTerms;

    @Column(name = "enabled")
    private short enabled;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userType.name())
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled == 1;
    }
}