package com.sitionix.ntfssox.consumer.mapper;

import com.app_afesox.ntfssox.events.notifications.contents.EmailVerificationContentDTO;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerifyContentEventMapperTest {

    private EmailVerifyContentEventMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = Mappers.getMapper(EmailVerifyContentEventMapper.class);
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
