package com.sitionix.ntfssox.application.handler;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("email-verification")
public class EmailVerificationHandler extends AbstractNotificationHandler<EmailVerifyContent> {

    public EmailVerificationHandler(final MessageProperties props,
                                    final EmailVerificationLinkClient emailVerificationLinkClient,
                                    final AuthUserClient authUserClient) {
        super(props);
        this.emailVerificationLinkClient = emailVerificationLinkClient;
        this.authUserClient = authUserClient;
    }

    private final EmailVerificationLinkClient emailVerificationLinkClient;

    private final AuthUserClient authUserClient;

    @Override
    public void send(final Notification<? extends EmailVerifyContent> notification) {
        final EmailVerifyContent content = notification.getContent();
        log.info("EmailVerificationHandler.send start tokenId={}, pepperId={}",
                content.getVerificationTokenId(), content.getPepperId());

        final EmailVerificationLink link = this.emailVerificationLinkClient.issueEmailVerificationLink(content.getVerificationTokenId(), content.getPepperId());
        log.info("EmailVerificationHandler.issueLink done link={}", link);

        this.authUserClient.verifyEmail(link);
        log.info("EmailVerificationHandler.verifyEmail done");
    }
}
