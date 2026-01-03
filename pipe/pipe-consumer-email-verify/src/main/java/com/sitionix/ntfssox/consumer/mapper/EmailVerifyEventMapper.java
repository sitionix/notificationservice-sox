package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.athssox.events.emailverify.EmailVerifyEvent;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface EmailVerifyEventMapper {

    EmailVerifyPayload asPayload(final EmailVerifyEvent event);
}
