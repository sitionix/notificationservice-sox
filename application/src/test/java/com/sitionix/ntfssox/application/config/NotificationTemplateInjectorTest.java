package com.sitionix.ntfssox.application.config;

import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.NotificationHandler;
import com.sitionix.ntfssox.domain.model.NotificationTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationTemplateInjectorTest {

    @Mock
    private ApplicationContext context;

    private NotificationTemplateInjector subject;

    @BeforeEach
    void setUp() {
        this.subject = new NotificationTemplateInjector(this.context);
    }

    @AfterEach
    void tearDown() {
        NotificationTemplate.EMAIL_VERIFY.setHandler(null);
        verifyNoMoreInteractions(this.context);
    }

    @Test
    void givenMatchingHandler_whenInjectHandlers_thenSetsTemplateHandler() {
        //given
        final NotificationHandler<?> handler = getNotificationHandler();
        final Map<String, NotificationHandler<?>> handlers = Map.of("emailVerification", handler);
        final Notification<Object> notification = mock(Notification.class);

        when(this.context.getBeansOfType(NotificationHandler.class)).thenReturn(handlers);

        //when
        this.subject.injectHandlers();
        NotificationTemplate.EMAIL_VERIFY.send(notification);

        //then
        verify(this.context).getBeansOfType(NotificationHandler.class);
        assertThat(((TestNotificationHandler) handler).getReceived()).isEqualTo(notification);
        verifyNoMoreInteractions(notification);
    }

    @Test
    void givenMissingHandler_whenInjectHandlers_thenThrows() {
        //given
        final Map<String, NotificationHandler<?>> handlers = Map.of();

        when(this.context.getBeansOfType(NotificationHandler.class)).thenReturn(handlers);

        //when
        assertThatThrownBy(this.subject::injectHandlers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No NotificationHandler bean for template: emailVerification");

        //then
        verify(this.context).getBeansOfType(NotificationHandler.class);
    }

    private TestNotificationHandler getNotificationHandler() {
        final MessageProperties props = mock(MessageProperties.class);
        return new TestNotificationHandler(props);
    }

    private static class TestNotificationHandler extends AbstractNotificationHandler<Object> {

        private Notification<?> received;

        private TestNotificationHandler(final MessageProperties props) {
            super(props);
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
