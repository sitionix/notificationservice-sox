package com.sitionix.ntfssox.client;

import com.app_afesox.bffssox.client.api.AuthApi;
import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.client.mapper.EmailVerifyClientMapper;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUserClientImplTest {

    @Mock
    private AuthApi authApi;

    @Mock
    private EmailVerifyClientMapper emailVerifyClientMapper;

    private AuthUserClientImpl subject;

    @BeforeEach
    void setUp() {
        this.subject = new AuthUserClientImpl(this.authApi, this.emailVerifyClientMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.authApi, this.emailVerifyClientMapper);
    }

    @Test
    void verifyEmail_mapsAndCallsAuthApi() {
        final UUID siteId = UUID.randomUUID();
        final EmailVerificationLink link = EmailVerificationLink.builder()
                .token("token-1")
                .siteId(siteId)
                .build();
        final EmailVerificationDTO dto = new EmailVerificationDTO()
                .token("token-1")
                .siteId(siteId);

        when(this.emailVerifyClientMapper.asEmailVerificationDto(link))
                .thenReturn(dto);

        this.subject.verifyEmail(link);

        verify(this.emailVerifyClientMapper).asEmailVerificationDto(link);
        verify(this.authApi).verifyEmail(dto);
    }
}
