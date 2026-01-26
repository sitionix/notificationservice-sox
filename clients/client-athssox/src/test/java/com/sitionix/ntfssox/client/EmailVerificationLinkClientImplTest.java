package com.sitionix.ntfssox.client;

import com.app_afesox.athssox.client.api.AuthApi;
import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponseDTO;
import com.sitionix.ntfssox.client.mapper.EmailVerificationLinkClientMapper;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailVerificationLinkClientImplTest {

    @Mock
    private AuthApi authApi;

    @Mock
    private EmailVerificationLinkClientMapper clientMapper;

    private EmailVerificationLinkClient subject;

    @BeforeEach
    void setUp() {
        this.subject = new EmailVerificationLinkClientImpl(this.authApi,
                this.clientMapper);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.clientMapper,
                this.authApi);
    }

    @Test
    void issueEmailVerificationLink_nullTokenId_returnsNull() {
        final EmailVerificationLink result = this.subject.issueEmailVerificationLink(null, UUID.randomUUID());

        assertNull(result);
        verifyNoInteractions(this.authApi, this.clientMapper);
    }

    @Test
    void issueEmailVerificationLink_nullPepperId_returnsNull() {
        final EmailVerificationLink result = this.subject.issueEmailVerificationLink(UUID.randomUUID(), null);

        assertNull(result);
        verifyNoInteractions(this.authApi, this.clientMapper);
    }

    @Test
    void issueEmailVerificationLink_responseNull_returnsNull() {
        final UUID tokenId = UUID.randomUUID();
        final UUID pepperId = UUID.randomUUID();

        when(this.authApi.issueEmailVerificationLink(tokenId, pepperId))
                .thenReturn(null);

        final EmailVerificationLink result = this.subject.issueEmailVerificationLink(tokenId, pepperId);

        assertNull(result);
        verify(this.authApi).issueEmailVerificationLink(tokenId, pepperId);
        verifyNoInteractions(this.clientMapper);
    }

    @Test
    void issueEmailVerificationLink_verifyUrlNull_returnsNull() {
        final UUID tokenId = UUID.randomUUID();
        final UUID pepperId = UUID.randomUUID();
        final IssueEmailVerificationLinkResponseDTO response = new IssueEmailVerificationLinkResponseDTO();

        when(this.authApi.issueEmailVerificationLink(tokenId, pepperId))
                .thenReturn(response);

        final EmailVerificationLink result = this.subject.issueEmailVerificationLink(tokenId, pepperId);

        assertNull(result);
        verify(this.authApi).issueEmailVerificationLink(tokenId, pepperId);
        verifyNoInteractions(this.clientMapper);
    }

    @Test
    void issueEmailVerificationLink_verifyUrlPresent_returnsString() {
        final UUID tokenId = UUID.randomUUID();
        final UUID pepperId = UUID.randomUUID();
        final IssueEmailVerificationLinkResponseDTO response = new IssueEmailVerificationLinkResponseDTO()
                .token("token-1");
        final EmailVerificationLink expected = EmailVerificationLink.builder()
                .token("token-1")
                .build();

        when(this.authApi.issueEmailVerificationLink(tokenId, pepperId))
                .thenReturn(response);
        when(this.clientMapper.asEmailVerificationLink(response))
                .thenReturn(expected);

        final EmailVerificationLink result = this.subject.issueEmailVerificationLink(tokenId, pepperId);

        assertEquals(expected, result);
        verify(this.authApi).issueEmailVerificationLink(tokenId, pepperId);
        verify(this.clientMapper).asEmailVerificationLink(response);
    }
}
