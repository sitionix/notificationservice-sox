package com.sitionix.ntfssox.client;

import com.app_afesox.bffssox.client.api.AuthApi;
import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.client.mapper.EmailVerifyClientMapper;
import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
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

    private final EmailVerifyClientMapper emailVerifyClientMapper;

    private final EmailVerificationLinkClient emailVerificationLinkClient;

    @Override
    public void verifyEmail(final EmailVerifyPayload payload) {
        if (payload == null || payload.getMeta() == null) {
            log.warn("Email verification payload is missing required fields: {}", payload);
            return;
        }

        final UUID tokenId = payload.getParams() != null ? payload.getParams().getEmailVerificationTokenId() : null;
        final UUID pepperId = payload.getParams() != null ? payload.getParams().getPepperId() : null;
        final String verifyUrl = resolveVerifyUrl(tokenId, pepperId, payload);

        final EmailVerificationDTO request =
                this.emailVerifyClientMapper.asEmailVerificationDto(payload, verifyUrl);
        if (request == null || request.getToken() == null || request.getToken().isBlank()) {
            log.warn("Email verification token not resolved for tokenId={}", tokenId);
            return;
        }
        this.authApi.verifyEmail(request);
    }

    private String resolveVerifyUrl(final UUID tokenId,
                                    final UUID pepperId,
                                    final EmailVerifyPayload payload) {
        if (tokenId != null && pepperId != null) {
            return this.emailVerificationLinkClient.issueEmailVerificationLink(tokenId, pepperId);
        }
        if (payload == null || payload.getParams() == null) {
            return null;
        }
        return payload.getParams().getVerifyUrl();
    }
}
