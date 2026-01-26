package com.sitionix.ntfssox.client;

import com.app_afesox.athssox.client.api.AuthApi;
import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponseDTO;
import com.sitionix.ntfssox.client.mapper.EmailVerificationLinkClientMapper;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationLinkClientImpl implements EmailVerificationLinkClient {

    private final AuthApi authApi;

    private final EmailVerificationLinkClientMapper clientMapper;

    @Override
    public EmailVerificationLink issueEmailVerificationLink(final UUID tokenId, final UUID pepperId) {
        if (tokenId == null || pepperId == null) {
            log.warn("Email verification link request missing token identifiers tokenId={}, pepperId={}", tokenId, pepperId);
            return null;
        }

        final IssueEmailVerificationLinkResponseDTO response =
                this.authApi.issueEmailVerificationLink(tokenId, pepperId);

        if (response == null) {
            log.warn("Email verification link response is null for tokenId={}, pepperId={}", tokenId, pepperId);
            return null;
        }
        if (response.getToken() == null || response.getToken().isBlank()) {
            log.warn("Email verification link response missing token for tokenId={}, pepperId={}",
                    tokenId, pepperId);
            return null;
        }

        return this.clientMapper.asEmailVerificationLink(response);
    }
}
