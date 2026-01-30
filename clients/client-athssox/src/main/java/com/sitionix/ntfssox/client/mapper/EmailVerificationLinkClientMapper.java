package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponseDTO;
import com.sitionix.ntfssox.domain.config.MapperSupport;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerificationLinkClientMapper extends MapperSupport {

    @Mapping(target = "expiresAt", expression = "java(toInstant(dto.getExpiresAt()))")
    EmailVerificationLink asEmailVerificationLink(IssueEmailVerificationLinkResponseDTO dto);
}
