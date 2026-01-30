package com.sitionix.ntfssox.consumer.mapper.registry;

import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.sitionix.ntfssox.consumer.mapper.EventContentMapper;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationMapperTest {

    @Mock
    private EventContentMapper<EmailVerifyContent, EmailVerificationContentDTO> contentMapper;

    private NotificationMapper subject;

    @BeforeEach
    void setUp() {
        when(this.contentMapper.supports()).thenReturn(EmailVerificationContentDTO.class);
        this.subject = new NotificationMapper(List.of(this.contentMapper));
    }

    @AfterEach
    void tearDown() {
        verify(this.contentMapper).supports();
        verifyNoMoreInteractions(this.contentMapper);
    }

    @Test
    void givenMatchingMapper_whenAsNotification_thenDelegatesToMapper() {
        //given
        final EmailVerificationContentDTO event = mock(EmailVerificationContentDTO.class);
        final EmailVerifyContent expected = mock(EmailVerifyContent.class);

        when(this.contentMapper.asNotification(event)).thenReturn(expected);

        //when
        final EmailVerifyContent actual = this.subject.asNotification(event);

        //then
        assertThat(actual).isSameAs(expected);
        verify(this.contentMapper).asNotification(event);
        verifyNoMoreInteractions(event, expected);
    }

    @Test
    void givenNullEvent_whenAsNotification_thenReturnsNull() {
        //given
        final EmailVerificationContentDTO event = null;

        //when
        final EmailVerifyContent actual = this.subject.asNotification(event);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void givenAssignableMapper_whenAsNotification_thenUsesAssignable() {
        //given
        final EventContentMapper<String, Number> assignableMapper = mock(EventContentMapper.class);
        final Integer event = 5;
        final String expected = "mapped-content";

        when(assignableMapper.supports()).thenReturn(Number.class);
        when(assignableMapper.asNotification(event)).thenReturn(expected);
        final NotificationMapper assignableSubject = new NotificationMapper(List.of(assignableMapper));

        //when
        final String actual = assignableSubject.asNotification(event);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(assignableMapper, atLeastOnce()).supports();
        verify(assignableMapper).asNotification(event);
        verifyNoMoreInteractions(assignableMapper);
    }

    @Test
    void givenNoMapperFound_whenAsNotification_thenThrows() {
        //given
        final String event = "event";

        //when
        final Throwable thrown = catchThrowable(() -> this.subject.asNotification(event));

        //then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.class.getName());
    }

    @Test
    void givenDuplicateMapperSupport_whenConstruct_thenThrows() {
        //given
        final EventContentMapper<Object, Object> firstMapper = mock(EventContentMapper.class);
        final EventContentMapper<Object, Object> secondMapper = mock(EventContentMapper.class);

        when(firstMapper.supports()).thenReturn(Object.class);
        when(secondMapper.supports()).thenReturn(Object.class);

        //when
        final Throwable thrown = catchThrowable(() -> new NotificationMapper(List.of(firstMapper, secondMapper)));

        //then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(Object.class.getName());
        verify(firstMapper, atLeastOnce()).supports();
        verify(secondMapper, atLeastOnce()).supports();
        verifyNoMoreInteractions(firstMapper, secondMapper);
    }
}
