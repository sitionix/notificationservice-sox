package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponseDTO;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerificationLinkClientMapper {

    @Mapping(target = "expiresAt", expression = "java(toInstant(dto.getExpiresAt()))")
    EmailVerificationLink asEmailVerificationLink(IssueEmailVerificationLinkResponseDTO dto);

    default Instant toInstant(final OffsetDateTime value) {
        return value != null ? value.toInstant() : null;
    }
}
