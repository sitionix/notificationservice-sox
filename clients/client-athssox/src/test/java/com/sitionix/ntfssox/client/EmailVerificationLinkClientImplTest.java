package com.sitionix.ntfssox.client;

import com.app_afesox.athssox.client.api.AuthApi;
import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponse;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailVerificationLinkClientImplTest {

    @Mock
    private AuthApi authApi;

    @InjectMocks
    private EmailVerificationLinkClientImpl subject;

    @Test
    void issueEmailVerificationLink_nullTokenId_returnsNull() {
        final String result = this.subject.issueEmailVerificationLink(null, UUID.randomUUID());

        assertNull(result);
        verifyNoInteractions(this.authApi);
    }

    @Test
    void issueEmailVerificationLink_nullPepperId_returnsNull() {
        final String result = this.subject.issueEmailVerificationLink(UUID.randomUUID(), null);

        assertNull(result);
        verifyNoInteractions(this.authApi);
    }

    @Test
    void issueEmailVerificationLink_responseNull_returnsNull() {
        final UUID tokenId = UUID.randomUUID();
        final UUID pepperId = UUID.randomUUID();

        when(this.authApi.issueEmailVerificationLink(tokenId, pepperId))
                .thenReturn(null);

        final String result = this.subject.issueEmailVerificationLink(tokenId, pepperId);

        assertNull(result);
        verify(this.authApi).issueEmailVerificationLink(tokenId, pepperId);
    }

    @Test
    void issueEmailVerificationLink_verifyUrlNull_returnsNull() {
        final UUID tokenId = UUID.randomUUID();
        final UUID pepperId = UUID.randomUUID();
        final IssueEmailVerificationLinkResponse response = new IssueEmailVerificationLinkResponse();

        when(this.authApi.issueEmailVerificationLink(tokenId, pepperId))
                .thenReturn(response);

        final String result = this.subject.issueEmailVerificationLink(tokenId, pepperId);

        assertNull(result);
        verify(this.authApi).issueEmailVerificationLink(tokenId, pepperId);
    }

    @Test
    void issueEmailVerificationLink_verifyUrlPresent_returnsString() {
        final UUID tokenId = UUID.randomUUID();
        final UUID pepperId = UUID.randomUUID();
        final IssueEmailVerificationLinkResponse response = new IssueEmailVerificationLinkResponse()
                .verifyUrl(URI.create("http://localhost/verify?token=token-1"));

        when(this.authApi.issueEmailVerificationLink(tokenId, pepperId))
                .thenReturn(response);

        final String result = this.subject.issueEmailVerificationLink(tokenId, pepperId);

        assertEquals("http://localhost/verify?token=token-1", result);
        verify(this.authApi).issueEmailVerificationLink(tokenId, pepperId);
    }
}
