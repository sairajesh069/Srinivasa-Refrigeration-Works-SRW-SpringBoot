package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query("SELECT n from Notification n WHERE n.userId = :userId OR n.userId = 'ALL_USERS' ORDER BY n.createdAt DESC")
    List<Notification> findAllByUserId(@Param("userId") String userId);

    List<Notification> findAllByOrderByCreatedAtDesc();
}