package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerifyClientMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "siteId", source = "payload.meta.siteId")
    EmailVerificationDTO asEmailVerificationDto(Notification payload, String token);
}
