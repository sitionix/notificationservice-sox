package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.athssox.events.emailverify.EmailVerifyEvent;
import com.app_afesox.athssox.events.emailverify.Params;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerifyEventMapper {

    EmailVerifyPayload asPayload(final EmailVerifyEvent event);

    @Mapping(target = "emailVerificationTokenId", source = "verificationTokenId")
    EmailVerifyPayload.Params asParams(final Params params);
}
