package com.sitionix.ntfssox.client.mapper;

import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmailVerifyClientMapperTest {

    private final EmailVerifyClientMapper mapper = Mappers.getMapper(EmailVerifyClientMapper.class);

    @Test
    void asEmailVerificationDto_nullPayloadAndUrl_returnsNull() {
        final EmailVerificationDTO dto = this.mapper.asEmailVerificationDto((EmailVerifyPayload) null, null);

        assertNull(dto);
    }

    @Test
    void asEmailVerificationDto_usesPayloadVerifyUrl() {
        final UUID siteId = UUID.randomUUID();
        final EmailVerifyPayload payload = EmailVerifyPayload.builder()
                .meta(EmailVerifyPayload.Meta.builder().siteId(siteId).build())
                .params(EmailVerifyPayload.Params.builder()
                        .verifyUrl("http://localhost/verify?token=token-1")
                        .build())
                .build();

        final EmailVerificationDTO dto = this.mapper.asEmailVerificationDto(payload);

        assertNotNull(dto);
        assertEquals("token-1", dto.getToken());
        assertEquals(siteId, dto.getSiteId());
    }

    @Test
    void extractToken_prefersExplicitVerifyUrlAndDecodes() {
        final String token = this.mapper.extractToken(null, "http://localhost/verify?token=a%2Bb");

        assertEquals("a+b", token);
    }

    @Test
    void extractToken_handlesTokenWithoutValue() {
        final String token = this.mapper.extractToken(null, "http://localhost/verify?token");

        assertEquals("", token);
    }

    @Test
    void extractToken_handlesInvalidUriFallback() {
        final String token = this.mapper.extractToken(null, "http://localhost/%?token=raw%3D1");

        assertEquals("raw=1", token);
    }

    @Test
    void extractToken_returnsNullWhenNoQuery() {
        final String token = this.mapper.extractToken(null, "http://localhost/verify");

        assertNull(token);
    }

    @Test
    void extractToken_returnsRawWhenDecodeFails() {
        final String token = this.mapper.extractToken(null, "http://localhost/verify?token=%ZZ");

        assertEquals("%ZZ", token);
    }
}
