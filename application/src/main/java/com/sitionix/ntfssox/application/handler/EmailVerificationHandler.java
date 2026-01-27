package com.sitionix.ntfssox.application.handler;

import com.sitionix.ntfssox.domain.model.AbstractNotificationHandler;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import com.sitionix.ntfssox.domain.usecase.VerifyEmail;
import org.springframework.stereotype.Component;

@Component("email-verification")
public class EmailVerificationHandler extends AbstractNotificationHandler<EmailVerifyContent> {

    public EmailVerificationHandler(final MessageProperties props) {
        super(props);
    }

    @Override
    public void send(final Notification<? extends EmailVerifyContent> notification) {
    }
}
