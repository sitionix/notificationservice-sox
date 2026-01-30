package com.sitionix.ntfssox.consumer.mapper.registry;

import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.sitionix.ntfssox.consumer.mapper.EventContentMapper;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationMapperTest {

    @Nested
    class ExactMatchMapping {

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
            verifyNoMoreInteractions(this.contentMapper);
        }

        @Test
        void givenMatchingMapper_whenAsNotification_thenDelegatesToMapper() {
            //given
            final EmailVerificationContentDTO event = getEmailVerificationContentDto();
            final EmailVerifyContent expected = getEmailVerifyContent();

            when(this.contentMapper.asNotification(event)).thenReturn(expected);

            //when
            final EmailVerifyContent actual = this.subject.asNotification(event);

            //then
            assertThat(actual).isEqualTo(expected);
            verify(this.contentMapper).supports();
            verify(this.contentMapper).asNotification(event);
        }

        @Test
        void givenNullEvent_whenAsNotification_thenReturnsNull() {
            //given
            final EmailVerificationContentDTO event = null;

            //when
            final EmailVerifyContent actual = this.subject.asNotification(event);

            //then
            assertThat(actual).isNull();
            verify(this.contentMapper).supports();
        }
    }

    @Nested
    class AssignableMapping {

        @Mock
        private EventContentMapper<String, BaseContent> contentMapper;

        private NotificationMapper subject;

        @BeforeEach
        void setUp() {
            when(this.contentMapper.supports()).thenReturn(BaseContent.class);
            this.subject = new NotificationMapper(List.of(this.contentMapper));
        }

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(this.contentMapper);
        }

        @Test
        void givenAssignableMapper_whenAsNotification_thenUsesAssignable() {
            //given
            final DerivedContent event = getDerivedContent();
            final String expected = "mapped-content";

            when(this.contentMapper.asNotification(event)).thenReturn(expected);

            //when
            final String actual = this.subject.asNotification(event);

            //then
            assertThat(actual).isEqualTo(expected);
            verify(this.contentMapper).supports();
            verify(this.contentMapper).asNotification(event);
        }
    }

    @Nested
    class MissingMapper {

        @Mock
        private EventContentMapper<EmailVerifyContent, EmailVerificationContentDTO> contentMapper;

        private NotificationMapper subject;

        @BeforeEach
        void setUp() {
            when(this.contentMapper.supports()).thenReturn(BaseContent.class);
            this.subject = new NotificationMapper(List.of(this.contentMapper));
        }

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(this.contentMapper);
        }

        @Test
        void givenNoMapperFound_whenAsNotification_thenThrows() {
            //given
            final EmailVerificationContentDTO event = getEmailVerificationContentDto();

            //when
            final Throwable thrown = catchThrowable(() -> this.subject.asNotification(event));

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(EmailVerificationContentDTO.class.getName());
            verify(this.contentMapper).supports();
        }
    }

    @Nested
    class DuplicateMapperSupport {

        @Mock
        private EventContentMapper<Object, Object> firstMapper;

        @Mock
        private EventContentMapper<Object, Object> secondMapper;

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(this.firstMapper, this.secondMapper);
        }

        @Test
        void givenDuplicateMapperSupport_whenConstruct_thenThrows() {
            //given
            when(this.firstMapper.supports()).thenReturn(Object.class);
            when(this.secondMapper.supports()).thenReturn(Object.class);

            //when
            final Throwable thrown = catchThrowable(() -> new NotificationMapper(List.of(this.firstMapper, this.secondMapper)));

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(Object.class.getName());
            verify(this.firstMapper).supports();
            verify(this.secondMapper).supports();
        }
    }

    private EmailVerificationContentDTO getEmailVerificationContentDto() {
        return new EmailVerificationContentDTO(
                "55555555-5555-5555-5555-555555555555",
                "66666666-6666-6666-6666-666666666666"
        );
    }

    private EmailVerifyContent getEmailVerifyContent() {
        return EmailVerifyContent.builder()
                .verificationTokenId(UUID.fromString("55555555-5555-5555-5555-555555555555"))
                .pepperId(UUID.fromString("66666666-6666-6666-6666-666666666666"))
                .build();
    }

    private DerivedContent getDerivedContent() {
        return new DerivedContent();
    }

    private static class BaseContent {
    }

    private static class DerivedContent extends BaseContent {
    }
}
