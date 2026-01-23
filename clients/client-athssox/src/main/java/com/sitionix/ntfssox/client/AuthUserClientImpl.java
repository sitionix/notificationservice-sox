package com.sitionix.ntfssox.client;

import com.app_afesox.athssox.client.api.AuthApi;
import com.app_afesox.athssox.client.dto.EmailVerificationDTO;
import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponse;
import com.sitionix.ntfssox.client.mapper.EmailVerificationClientMapper;
import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserClientImpl implements AuthUserClient {

    private final AuthApi authApi;

    private final EmailVerificationClientMapper emailVerificationClientMapper;

    @Override
    public void verifyEmail(final EmailVerifyPayload payload) {
        if (payload == null || payload.getParams() == null || payload.getMeta() == null) {
            log.warn("Email verification payload is missing required fields: {}", payload);
            return;
        }

        final UUID tokenId = payload.getParams().getEmailVerificationTokenId();
        final UUID pepperId = payload.getParams().getPepperId();
        final IssueEmailVerificationLinkResponse issueResponse = resolveIssueLink(tokenId, pepperId, payload);
        final EmailVerificationDTO request =
                this.emailVerificationClientMapper.asEmailVerificationDto(payload, issueResponse);

        if (request == null || request.getToken() == null || request.getToken().isBlank()) {
            log.warn("Email verification token not resolved for tokenId={}", tokenId);
            return;
        }

        this.authApi.verifyEmail(request);
    }

    private IssueEmailVerificationLinkResponse resolveIssueLink(final UUID tokenId,
                                                               final UUID pepperId,
                                                               final EmailVerifyPayload payload) {
        if (tokenId == null || pepperId == null) {
            log.warn("Email verification payload is missing token identifiers: {}", payload);
            return null;
        }
        return this.authApi.issueEmailVerificationLink(tokenId, pepperId);
    }
}
