package com.sitionix.ntfssox.consumer.mapper.uses;

import com.app_afesox.ntfssox.events.notifications.NotificationTemplateDTO;
import com.sitionix.ntfssox.domain.config.MapstructModel;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapstructModel.COMPONENT_MODEL)
public interface NotificationTemplateMapper {

    default NotificationTemplate asTemplate(final NotificationTemplateDTO templateDTO) {
        if (templateDTO == null) {
            return null;
        }
        return NotificationTemplate.valueOf(templateDTO.name());
    }
}
