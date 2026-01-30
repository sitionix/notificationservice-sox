package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.ntfssox.events.notifications.NotificationEvent;
import com.sitionix.ntfssox.consumer.mapper.registry.NotificationMapper;
import com.sitionix.ntfssox.domain.model.Notification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationEventMapperTest {

    @Mock
    private NotificationMapper contentMapper;

    private TestNotificationEventMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = new TestNotificationEventMapper();
        this.subject.contentMapper = this.contentMapper;
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.contentMapper);
    }

    @Test
    void givenNullContent_whenMapContent_thenReturnsNull() {
        //given
        final Object content = null;

        //when
        final Object result = this.subject.mapContentPublic(content);

        //then
        assertThat(result).isNull();
    }

    @Test
    void givenContent_whenMapContent_thenDelegatesToContentMapper() {
        //given
        final Object content = getContent();
        final Object mapped = getMappedContent();

        when(this.contentMapper.asNotification(content)).thenReturn(mapped);

        //when
        final Object result = this.subject.mapContentPublic(content);

        //then
        assertThat(result).isEqualTo(mapped);
        verify(this.contentMapper).asNotification(content);
    }

    private Object getContent() {
        return new Object();
    }

    private Object getMappedContent() {
        return "mapped-content";
    }

    private static class TestNotificationEventMapper extends NotificationEventMapper {

        @Override
        public Notification<Object> asNotification(final NotificationEvent event) {
            return null;
        }

        Object mapContentPublic(final Object content) {
            return mapContent(content);
        }
    }
}
