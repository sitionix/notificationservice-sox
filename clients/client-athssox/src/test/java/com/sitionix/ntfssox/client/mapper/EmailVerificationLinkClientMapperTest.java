package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponseDTO;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmailVerificationLinkClientMapperTest {

    private EmailVerificationLinkClientMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = new EmailVerificationLinkClientMapperImpl();
    }

    @Test
    void asEmailVerificationLink_nullDto_returnsNull() {
        final EmailVerificationLink result = this.subject.asEmailVerificationLink(null);

        assertNull(result);
    }

    @Test
    void asEmailVerificationLink_mapsAllFields() {
        final UUID tokenId = UUID.randomUUID();
        final UUID siteId = UUID.randomUUID();
        final OffsetDateTime expiresAt = OffsetDateTime.of(2099, 1, 2, 12, 0, 0, 0, ZoneOffset.UTC);
        final IssueEmailVerificationLinkResponseDTO dto = new IssueEmailVerificationLinkResponseDTO()
                .token("token-1")
                .tokenId(tokenId)
                .siteId(siteId)
                .expiresAt(expiresAt);

        final EmailVerificationLink result = this.subject.asEmailVerificationLink(dto);

        assertEquals("token-1", result.getToken());
        assertEquals(tokenId, result.getTokenId());
        assertEquals(siteId, result.getSiteId());
        assertEquals(expiresAt.toInstant(), result.getExpiresAt());
    }

    @Test
    void asEmailVerificationLink_nullExpiresAt_mapsNull() {
        final IssueEmailVerificationLinkResponseDTO dto = new IssueEmailVerificationLinkResponseDTO()
                .token("token-1");

        final EmailVerificationLink result = this.subject.asEmailVerificationLink(dto);

        assertNull(result.getExpiresAt());
    }
}
