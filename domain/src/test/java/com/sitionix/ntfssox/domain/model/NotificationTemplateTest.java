package com.sitionix.ntfssox.domain.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class NotificationTemplateTest {

    private NotificationTemplate subject;

    @BeforeEach
    void setUp() {
        this.subject = NotificationTemplate.EMAIL_VERIFY;
    }

    @AfterEach
    void tearDown() {
        NotificationTemplate.EMAIL_VERIFY.setHandler(null);
    }

    @Test
    void givenNotification_whenSend_thenDelegatesToHandler() {
        //given
        final Notification<Object> notification = mock(Notification.class);
        final MessageProperties messageProperties = mock(MessageProperties.class);
        final TestNotificationHandler notificationHandler = new TestNotificationHandler(messageProperties);
        this.subject.setHandler(notificationHandler);

        //when
        this.subject.send(notification);

        //then
        assertThat(notificationHandler.getReceived()).isEqualTo(notification);
        verifyNoMoreInteractions(notification, messageProperties);
    }

    @Test
    void givenBindingKey_whenByBindingKey_thenReturnsTemplate() {
        //given
        final String bindingKey = "email-verification";

        //when
        final NotificationTemplate actual = NotificationTemplate.byBindingKey(bindingKey);

        //then
        assertThat(actual).isEqualTo(NotificationTemplate.EMAIL_VERIFY);
    }

    @Test
    void givenUnknownBindingKey_whenByBindingKey_thenReturnsNull() {
        //given
        final String bindingKey = "missing";

        //when
        final NotificationTemplate actual = NotificationTemplate.byBindingKey(bindingKey);

        //then
        assertThat(actual).isNull();
    }

    private static class TestNotificationHandler extends AbstractNotificationHandler<Object> {

        private Notification<?> received;

        private TestNotificationHandler(final MessageProperties messageProperties) {
            super(messageProperties);
        }

        @Override
        public void send(final Notification<? extends Object> notification) {
            this.received = notification;
        }

        private Notification<?> getReceived() {
            return this.received;
        }
    }
}
