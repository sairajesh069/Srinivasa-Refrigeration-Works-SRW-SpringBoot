package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
