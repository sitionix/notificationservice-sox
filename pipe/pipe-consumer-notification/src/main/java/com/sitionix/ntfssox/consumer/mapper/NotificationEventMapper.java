package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.ntfssox.events.notifications.NotificationEvent;
import com.sitionix.ntfssox.consumer.mapper.registry.NotificationMapper;
import com.sitionix.ntfssox.consumer.mapper.uses.NotificationTemplateMapper;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.Notification;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL,
        uses = {
                NotificationTemplateMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class NotificationEventMapper {

    protected NotificationMapper contentMapper;

    @Autowired
    void setContentMapper(final NotificationMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    @Mapping(target = "content", expression = "java(mapContent(event.getContent()))")
    public abstract Notification<Object> asNotification(final NotificationEvent event);

    protected Object mapContent(final Object content) {
        return content == null ? null : contentMapper.asNotification(content);
    }
}
