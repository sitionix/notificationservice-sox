package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.ntfssox.events.notifications.DeliveryDTO;
import com.app_afesox.ntfssox.events.notifications.MetaDTO;
import com.app_afesox.ntfssox.events.notifications.NotificationChannelDTO;
import com.app_afesox.ntfssox.events.notifications.NotificationEvent;
import com.app_afesox.ntfssox.events.notifications.NotificationTemplateDTO;
import com.sitionix.ntfssox.consumer.mapper.registry.NotificationMapper;
import com.sitionix.ntfssox.consumer.mapper.uses.NotificationTemplateMapper;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import com.sitionix.ntfssox.domain.model.VerifyChannel;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationEventMapperTest {

    @Mock
    private NotificationMapper contentMapper;

    @Mock
    private NotificationTemplateMapper notificationTemplateMapper;

    private NotificationEventMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = new NotificationEventMapperImpl(this.notificationTemplateMapper, this.contentMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.contentMapper, this.notificationTemplateMapper);
    }

    @Test
    void givenNotificationEvent_whenAsNotification_thenMapsToDomain() {
        //given
        final Object content = mock(Object.class);
        final Object mappedContent = mock(Object.class);
        final NotificationEvent event = getNotificationEvent(content);
        final NotificationTemplate template = NotificationTemplate.EMAIL_VERIFY;

        when(this.notificationTemplateMapper.asTemplate(NotificationTemplateDTO.EMAIL_VERIFY)).thenReturn(template);
        when(this.contentMapper.asNotification(content)).thenReturn(mappedContent);

        final Notification<Object> expected = getExpectedNotification(mappedContent, template);

        //when
        final Notification<Object> actual = this.subject.asNotification(event);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(this.notificationTemplateMapper).asTemplate(NotificationTemplateDTO.EMAIL_VERIFY);
        verify(this.contentMapper).asNotification(content);
        verifyNoMoreInteractions(content, mappedContent);
    }

    @Test
    void givenNullEvent_whenAsNotification_thenReturnsNull() {
        //given
        final NotificationEvent event = null;

        //when
        final Notification<Object> actual = this.subject.asNotification(event);

        //then
        assertThat(actual).isNull();
    }

    private NotificationEvent getNotificationEvent(final Object content) {
        return new NotificationEvent(
                getDeliveryDto(),
                NotificationTemplateDTO.EMAIL_VERIFY,
                content,
                getMetaDto()
        );
    }

    private DeliveryDTO getDeliveryDto() {
        return new DeliveryDTO(NotificationChannelDTO.EMAIL, "user@example.com");
    }

    private MetaDTO getMetaDto() {
        return new MetaDTO(
                1L,
                "55555555-5555-5555-5555-555555555555",
                "trace-1",
                "2024-01-01T10:15:30Z"
        );
    }

    private Notification<Object> getExpectedNotification(final Object mappedContent, final NotificationTemplate template) {
        return Notification.builder()
                .delivery(getExpectedDelivery())
                .template(template)
                .content(mappedContent)
                .meta(getExpectedMeta())
                .build();
    }

    private Notification.Delivery getExpectedDelivery() {
        return Notification.Delivery.builder()
                .channel(VerifyChannel.EMAIL)
                .to("user@example.com")
                .build();
    }

    private Notification.Meta getExpectedMeta() {
        return Notification.Meta.builder()
                .userId(1L)
                .siteId(UUID.fromString("55555555-5555-5555-5555-555555555555"))
                .traceId("trace-1")
                .requestedAt(Instant.parse("2024-01-01T10:15:30Z"))
                .build();
    }

}
