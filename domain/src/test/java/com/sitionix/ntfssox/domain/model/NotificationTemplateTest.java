package com.sitionix.ntfssox.domain.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        final NotificationHandler<Object> notificationHandler = mock(NotificationHandler.class);
        this.subject.setHandler(notificationHandler);

        //when
        this.subject.send(notification);

        //then
        verify(notificationHandler).send(notification);
        verifyNoMoreInteractions(notificationHandler, notification);
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

}
