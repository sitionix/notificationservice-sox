package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.sitionix.ntfssox.domain.config.MapperSupport;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerifyContentEventMapper
        extends EventContentMapper<EmailVerifyContent, EmailVerificationContentDTO>, MapperSupport {

    @Override
    default Class<EmailVerificationContentDTO> supports() {
        return EmailVerificationContentDTO.class;
    }

    @Override
    @Mapping(target = "verificationTokenId", source = "verificationTokenId")
    @Mapping(target = "pepperId", source = "pepperId")
    EmailVerifyContent asNotification(EmailVerificationContentDTO event);

}
