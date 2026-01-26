package com.sitionix.ntfssox.client;

import com.app_afesox.bffssox.client.api.AuthApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthUserClientImplTest {

    @Mock
    private AuthApi authApi;

    private AuthUserClientImpl subject;

    @BeforeEach
    void setUp() {
        this.subject = new AuthUserClientImpl(this.authApi);
    }
//
//    @Test
//    void verifyEmail_missingMeta_noInteractions() {
//        final EmailVerifyPayload payload = EmailVerifyPayload.builder()
//                .params(EmailVerifyPayload.Params.builder().build())
//                .build();
//
//        this.subject.verifyEmail(payload);
//
//        verifyNoInteractions(this.authApi, this.emailVerifyClientMapper, this.emailVerificationLinkClient);
//    }
//
//    @Test
//    void verifyEmail_resolvesLinkAndCallsAuthApi() {
//        final UUID tokenId = UUID.randomUUID();
//        final UUID pepperId = UUID.randomUUID();
//        final UUID siteId = UUID.randomUUID();
//        final String verifyUrl = "http://localhost/verify?token=token-1";
//
//        final EmailVerifyPayload payload = EmailVerifyPayload.builder()
//                .meta(EmailVerifyPayload.Meta.builder().siteId(siteId).build())
//                .params(EmailVerifyPayload.Params.builder()
//                        .emailVerificationTokenId(tokenId)
//                        .pepperId(pepperId)
//                        .build())
//                .build();
//
//        final EmailVerificationDTO dto = new EmailVerificationDTO()
//                .token("token-1")
//                .siteId(siteId);
//
//        when(this.emailVerificationLinkClient.issueEmailVerificationLink(tokenId, pepperId))
//                .thenReturn(verifyUrl);
//        when(this.emailVerifyClientMapper.asEmailVerificationDto(payload, verifyUrl))
//                .thenReturn(dto);
//
//        this.subject.verifyEmail(payload);
//
//        verify(this.emailVerificationLinkClient).issueEmailVerificationLink(tokenId, pepperId);
//        verify(this.emailVerifyClientMapper).asEmailVerificationDto(payload, verifyUrl);
//        verify(this.authApi).verifyEmail(dto);
//    }
//
//    @Test
//    void verifyEmail_fallsBackToPayloadUrlWhenTokenIdsMissing() {
//        final UUID siteId = UUID.randomUUID();
//        final String verifyUrl = "http://localhost/verify?token=token-2";
//
//        final EmailVerifyPayload payload = EmailVerifyPayload.builder()
//                .meta(EmailVerifyPayload.Meta.builder().siteId(siteId).build())
//                .params(EmailVerifyPayload.Params.builder()
//                        .verifyUrl(verifyUrl)
//                        .build())
//                .build();
//
//        final EmailVerificationDTO dto = new EmailVerificationDTO()
//                .token("token-2")
//                .siteId(siteId);
//
//        when(this.emailVerifyClientMapper.asEmailVerificationDto(payload, verifyUrl))
//                .thenReturn(dto);
//
//        this.subject.verifyEmail(payload);
//
//        verify(this.emailVerifyClientMapper).asEmailVerificationDto(payload, verifyUrl);
//        verify(this.authApi).verifyEmail(dto);
//        verify(this.emailVerificationLinkClient, never()).issueEmailVerificationLink(any(), any());
//    }
//
//    @Test
//    void verifyEmail_doesNotCallAuthApiWhenTokenMissing() {
//        final UUID tokenId = UUID.randomUUID();
//        final UUID pepperId = UUID.randomUUID();
//        final UUID siteId = UUID.randomUUID();
//        final String verifyUrl = "http://localhost/verify?token=token-3";
//
//        final EmailVerifyPayload payload = EmailVerifyPayload.builder()
//                .meta(EmailVerifyPayload.Meta.builder().siteId(siteId).build())
//                .params(EmailVerifyPayload.Params.builder()
//                        .emailVerificationTokenId(tokenId)
//                        .pepperId(pepperId)
//                        .build())
//                .build();
//
//        final EmailVerificationDTO dto = new EmailVerificationDTO()
//                .token(" ")
//                .siteId(siteId);
//
//        when(this.emailVerificationLinkClient.issueEmailVerificationLink(tokenId, pepperId))
//                .thenReturn(verifyUrl);
//        when(this.emailVerifyClientMapper.asEmailVerificationDto(payload, verifyUrl))
//                .thenReturn(dto);
//
//        this.subject.verifyEmail(payload);
//
//        verify(this.emailVerificationLinkClient).issueEmailVerificationLink(tokenId, pepperId);
//        verify(this.emailVerifyClientMapper).asEmailVerificationDto(payload, verifyUrl);
//        verify(this.authApi, never()).verifyEmail(dto);
//    }
//
//    @Test
//    void verifyEmail_doesNotCallAuthApiWhenMapperReturnsNull() {
//        final UUID tokenId = UUID.randomUUID();
//        final UUID pepperId = UUID.randomUUID();
//        final UUID siteId = UUID.randomUUID();
//        final String verifyUrl = "http://localhost/verify?token=token-4";
//
//        final EmailVerifyPayload payload = EmailVerifyPayload.builder()
//                .meta(EmailVerifyPayload.Meta.builder().siteId(siteId).build())
//                .params(EmailVerifyPayload.Params.builder()
//                        .emailVerificationTokenId(tokenId)
//                        .pepperId(pepperId)
//                        .build())
//                .build();
//
//        when(this.emailVerificationLinkClient.issueEmailVerificationLink(tokenId, pepperId))
//                .thenReturn(verifyUrl);
//        when(this.emailVerifyClientMapper.asEmailVerificationDto(payload, verifyUrl))
//                .thenReturn(null);
//
//        this.subject.verifyEmail(payload);
//
//        verify(this.emailVerificationLinkClient).issueEmailVerificationLink(tokenId, pepperId);
//        verify(this.emailVerifyClientMapper).asEmailVerificationDto(payload, verifyUrl);
//        verify(this.authApi, never()).verifyEmail(any());
//    }
}
