package com.sitionix.ntfssox.client;

import com.app_afesox.bffssox.client.api.AuthApi;
import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.client.mapper.EmailVerifyClientMapper;
import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserClientImpl implements AuthUserClient {

    private final AuthApi authApi;
    private final EmailVerificationLinkClient emailVerificationLinkClient;
    private final EmailVerifyClientMapper emailVerifyClientMapper;

    @Override
    public void verifyEmail(final Notification payload) {
        if (payload == null || payload.getMeta() == null) {
            log.warn("Email verification payload is missing required fields: {}", payload);
            return;
        }
        if (!(payload.getContent() instanceof EmailVerifyContent content)) {
            log.warn("Email verification payload missing content: {}", payload);
            return;
        }

        final EmailVerificationLink link = this.emailVerificationLinkClient.issueEmailVerificationLink(
                content.getVerificationTokenId(),
                content.getPepperId()
        );

        if (link == null || link.getToken() == null || link.getToken().isBlank()) {
            log.warn("Email verification link token missing for content: {}", content);
            return;
        }

        final EmailVerificationDTO dto = this.emailVerifyClientMapper.asEmailVerificationDto(payload, link.getToken());
        if (dto == null || dto.getToken() == null || dto.getToken().isBlank()) {
            log.warn("Email verification DTO missing token for content: {}", content);
            return;
        }

        this.authApi.verifyEmail(dto);
    }
}
