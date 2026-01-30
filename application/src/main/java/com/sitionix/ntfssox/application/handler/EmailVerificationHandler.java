package com.sitionix.ntfssox.application.handler;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import org.springframework.stereotype.Component;

@Component("emailVerification")
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
        final EmailVerificationLink link = this.emailVerificationLinkClient.issueEmailVerificationLink(content.getVerificationTokenId(), content.getPepperId());
        this.authUserClient.verifyEmail(link);
    }
}
