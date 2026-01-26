package com.sitionix.ntfssox.application.usecase;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import com.sitionix.ntfssox.domain.usecase.VerifyEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyEmailImpl implements VerifyEmail {

    private final AuthUserClient authUserClient;

    private final EmailVerificationLinkClient emailVerificationLinkClient;

    @Override
    public void execute(final Notification payload) {
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

    }
}
