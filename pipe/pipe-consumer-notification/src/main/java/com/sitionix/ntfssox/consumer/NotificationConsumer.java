package com.sitionix.ntfssox.consumer;

import com.app_afesox.ntfssox.events.notifications.NotificationEnvelope;
import com.app_afesox.ntfssox.events.notifications.kafka.NotificationsV1ConsumerHandler;
import com.sitionix.ntfssox.consumer.mapper.NotificationEventMapper;
import com.sitionix.ntfssox.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer implements NotificationsV1ConsumerHandler {

    private final NotificationEventMapper notificationEventMapper;

    @Override
    public void consumeNotifications(final NotificationEnvelope notificationEnvelope) {
        log.info("Consuming notification event: {}", notificationEnvelope.getPayload().getTemplate());
        final Notification<?> notification = this.notificationEventMapper.asNotification(notificationEnvelope.getPayload());

        notification.getTemplate().send(notification);
    }
}
