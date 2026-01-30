package com.sitionix.ntfssox.consumer.mapper.registry;

import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.sitionix.ntfssox.consumer.mapper.EventContentMapper;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    void givenMatchingMapper_whenAsNotification_thenDelegatesToMapper() {
        //given
        final EventContentMapper<EmailVerifyContent, EmailVerificationContentDTO> contentMapper = mock(EventContentMapper.class);
        final NotificationMapper subject = new NotificationMapper(List.of(contentMapper));
        final EmailVerificationContentDTO event = mock(EmailVerificationContentDTO.class);
        final EmailVerifyContent expected = mock(EmailVerifyContent.class);

        when(contentMapper.supports()).thenReturn(EmailVerificationContentDTO.class);
        when(contentMapper.asNotification(event)).thenReturn(expected);

        //when
        final EmailVerifyContent actual = subject.asNotification(event);

        //then
        assertThat(actual).isSameAs(expected);
        verify(contentMapper).supports();
        verify(contentMapper).asNotification(event);
        verifyNoMoreInteractions(contentMapper, event, expected);
    }

    @Test
    void givenNullEvent_whenAsNotification_thenReturnsNull() {
        //given
        final EventContentMapper<EmailVerifyContent, EmailVerificationContentDTO> contentMapper = mock(EventContentMapper.class);
        final NotificationMapper subject = new NotificationMapper(List.of(contentMapper));
        final EmailVerificationContentDTO event = null;

        when(contentMapper.supports()).thenReturn(EmailVerificationContentDTO.class);

        //when
        final EmailVerifyContent actual = subject.asNotification(event);

        //then
        assertThat(actual).isNull();
        verify(contentMapper).supports();
        verifyNoMoreInteractions(contentMapper);
    }

    @Test
    void givenAssignableMapper_whenAsNotification_thenUsesAssignable() {
        //given
        final EventContentMapper<String, Number> assignableMapper = mock(EventContentMapper.class);
        final NotificationMapper subject = new NotificationMapper(List.of(assignableMapper));
        final Integer event = 5;
        final String expected = "mapped-content";

        when(assignableMapper.supports()).thenReturn(Number.class);
        when(assignableMapper.asNotification(event)).thenReturn(expected);

        //when
        final String actual = subject.asNotification(event);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(assignableMapper).supports();
        verify(assignableMapper).asNotification(event);
        verifyNoMoreInteractions(assignableMapper);
    }

    @Test
    void givenNoMapperFound_whenAsNotification_thenThrows() {
        //given
        final EventContentMapper<Object, Number> contentMapper = mock(EventContentMapper.class);
        final NotificationMapper subject = new NotificationMapper(List.of(contentMapper));
        final String event = "event";

        when(contentMapper.supports()).thenReturn(Number.class);

        //when
        final Throwable thrown = catchThrowable(() -> subject.asNotification(event));

        //then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.class.getName());
        verify(contentMapper).supports();
        verifyNoMoreInteractions(contentMapper);
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
