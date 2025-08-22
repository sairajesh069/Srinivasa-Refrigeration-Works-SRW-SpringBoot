package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.UserCredential;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {

    @Query("SELECT uc FROM UserCredential uc WHERE uc.username = :identifier OR uc.email = :identifier OR uc.phoneNumber = :identifier")
    Optional<UserCredential> findByIdentifier(@Param("identifier") String identifier);

    @Query("SELECT uc FROM UserCredential uc WHERE uc.username = :loginId OR uc.email = :loginId")
    UserCredential findByLoginId(@Param("loginId") String loginId);

    @Query("SELECT COUNT(uc) > 0 FROM UserCredential uc WHERE (uc.username = :loginId OR uc.email = :loginId) AND uc.phoneNumber = :phoneNumber")
    boolean existsByLoginIdAndPhoneNumber(@Param("loginId") String loginId, @Param("phoneNumber") String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE UserCredential uc SET uc.password = :password WHERE uc.username = :loginId OR uc.email = :loginId")
    void updatePassword(@Param("loginId") String loginId, @Param("password") String password);
}
