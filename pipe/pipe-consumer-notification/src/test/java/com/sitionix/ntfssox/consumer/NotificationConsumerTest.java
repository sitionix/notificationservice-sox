package com.sitionix.ntfssox.consumer;

import com.app_afesox.ntfssox.events.notifications.NotificationEnvelope;
import com.app_afesox.ntfssox.events.notifications.NotificationEvent;
import com.app_afesox.ntfssox.events.notifications.NotificationTemplateDTO;
import com.sitionix.ntfssox.consumer.mapper.NotificationEventMapper;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.NotificationHandler;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationEventMapper notificationEventMapper;

    private NotificationConsumer subject;

    @BeforeEach
    void setUp() {
        this.subject = new NotificationConsumer(this.notificationEventMapper);
    }

    @AfterEach
    void tearDown() {
        NotificationTemplate.EMAIL_VERIFY.setHandler(null);
        verifyNoMoreInteractions(this.notificationEventMapper);
    }

    @Test
    void givenNotificationEnvelope_whenConsumeNotifications_thenMapsAndSends() {
        //given
        final NotificationEnvelope notificationEnvelope = mock(NotificationEnvelope.class);
        final NotificationEvent notificationEvent = mock(NotificationEvent.class);
        final Notification<Object> notification = mock(Notification.class);
        final NotificationHandler<Object> notificationHandler = mock(NotificationHandler.class);
        NotificationTemplate.EMAIL_VERIFY.setHandler(notificationHandler);

        when(notificationEnvelope.getPayload()).thenReturn(notificationEvent);
        when(notificationEvent.getTemplate()).thenReturn(NotificationTemplateDTO.EMAIL_VERIFY);
        when(this.notificationEventMapper.asNotification(notificationEvent)).thenReturn(notification);
        when(notification.getTemplate()).thenReturn(NotificationTemplate.EMAIL_VERIFY);

        //when
        this.subject.consumeNotifications(notificationEnvelope);

        //then
        verify(notificationEnvelope, times(2)).getPayload();
        verify(notificationEvent).getTemplate();
        verify(this.notificationEventMapper).asNotification(notificationEvent);
        verify(notification).getTemplate();
        verify(notificationHandler).send(notification);
        verifyNoMoreInteractions(notificationEnvelope, notificationEvent, notification, notificationHandler);
    }
}
