package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmailVerifyClientMapperTest {

    private EmailVerifyClientMapper subject;

    @BeforeEach
    void setUp() {
        this.subject = new EmailVerifyClientMapperImpl();
    }

    @Test
    void asEmailVerificationDto_nullInput_returnsNull() {
        assertNull(this.subject.asEmailVerificationDto(null));
    }

    @Test
    void asEmailVerificationDto_mapsTokenAndSiteId() {
        final UUID siteId = UUID.randomUUID();
        final EmailVerificationLink link = EmailVerificationLink.builder()
                .token("token-1")
                .siteId(siteId)
                .build();

        final EmailVerificationDTO dto = this.subject.asEmailVerificationDto(link);

        assertNotNull(dto);
        assertEquals("token-1", dto.getToken());
        assertEquals(siteId, dto.getSiteId());
    }
}
