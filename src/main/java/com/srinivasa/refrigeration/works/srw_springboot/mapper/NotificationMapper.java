package com.srinivasa.refrigeration.works.srw_springboot.mapper;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Notification;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toEntity(NotificationDTO notificationDTO);

    NotificationDTO toDto(Notification notification);
}
