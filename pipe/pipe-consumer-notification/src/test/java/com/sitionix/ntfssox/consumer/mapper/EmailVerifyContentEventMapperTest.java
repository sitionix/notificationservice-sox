package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerifyContentEventMapperTest {

    private EmailVerifyContentEventMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = new EmailVerifyContentEventMapperImpl();
    }

    @Test
    void givenEmailVerificationContent_whenAsNotification_thenMapsToDomain() {
        //given
        final EmailVerificationContentDTO event = getEmailVerificationContentDto();
        final EmailVerifyContent expected = getEmailVerifyContent();

        //when
        final EmailVerifyContent actual = this.subject.asNotification(event);

        //then
        assertThat(actual).isEqualTo(expected);
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
    void givenMapper_whenSupports_thenReturnsEventClass() {
        //given
        final Class<EmailVerificationContentDTO> expected = EmailVerificationContentDTO.class;

        //when
        final Class<EmailVerificationContentDTO> actual = this.subject.supports();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private EmailVerificationContentDTO getEmailVerificationContentDto() {
        return new EmailVerificationContentDTO(
                "33333333-3333-3333-3333-333333333333",
                "44444444-4444-4444-4444-444444444444"
        );
    }

    private EmailVerifyContent getEmailVerifyContent() {
        return EmailVerifyContent.builder()
                .verificationTokenId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                .pepperId(UUID.fromString("44444444-4444-4444-4444-444444444444"))
                .build();
    }
}
