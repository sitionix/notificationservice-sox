package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerifyClientMapper {

    EmailVerificationDTO asEmailVerificationDto(EmailVerificationLink emailVerification);
}
