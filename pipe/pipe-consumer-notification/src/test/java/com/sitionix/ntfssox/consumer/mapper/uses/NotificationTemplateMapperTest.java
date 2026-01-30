package com.sitionix.ntfssox.consumer.mapper.uses;

import com.app_afesox.ntfssox.events.notifications.NotificationTemplateDTO;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTemplateMapperTest {

    private NotificationTemplateMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = new NotificationTemplateMapper() {
        };
    }

    @Test
    void givenTemplateDto_whenAsTemplate_thenMapsToDomain() {
        //given
        final NotificationTemplateDTO templateDTO = NotificationTemplateDTO.EMAIL_VERIFY;

        //when
        final NotificationTemplate actual = this.subject.asTemplate(templateDTO);

        //then
        assertThat(actual).isEqualTo(NotificationTemplate.EMAIL_VERIFY);
    }

    @Test
    void givenNullTemplateDto_whenAsTemplate_thenReturnsNull() {
        //given
        final NotificationTemplateDTO templateDTO = null;

        //when
        final NotificationTemplate actual = this.subject.asTemplate(templateDTO);

        //then
        assertThat(actual).isNull();
    }
}
