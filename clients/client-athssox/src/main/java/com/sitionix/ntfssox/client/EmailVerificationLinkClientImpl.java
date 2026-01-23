package com.sitionix.ntfssox.client;

import com.app_afesox.athssox.client.api.AuthApi;
import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponse;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationLinkClientImpl implements EmailVerificationLinkClient {

    private final AuthApi authApi;

    @Override
    public String issueEmailVerificationLink(final UUID tokenId, final UUID pepperId) {
        if (tokenId == null || pepperId == null) {
            log.warn("Email verification link request missing token identifiers tokenId={}, pepperId={}", tokenId, pepperId);
            return null;
        }

        final IssueEmailVerificationLinkResponse response =
                this.authApi.issueEmailVerificationLink(tokenId, pepperId);
        if (response == null) {
            return null;
        }
        final URI verifyUrl = response.getVerifyUrl();
        return verifyUrl != null ? verifyUrl.toString() : null;
    }
}
