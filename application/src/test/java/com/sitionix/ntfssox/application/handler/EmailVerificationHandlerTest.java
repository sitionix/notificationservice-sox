package com.sitionix.ntfssox.application.handler;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.client.EmailVerificationLinkClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import com.sitionix.ntfssox.domain.model.MessageProperties;
import com.sitionix.ntfssox.domain.model.Notification;
import com.sitionix.ntfssox.domain.model.content.EmailVerifyContent;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailVerificationHandlerTest {

    @Mock
    private EmailVerificationLinkClient emailVerificationLinkClient;

    @Mock
    private AuthUserClient authUserClient;

    @Mock
    private MessageProperties messageProperties;

    private EmailVerificationHandler subject;

    @BeforeEach
    void setUp() {
        this.subject = new EmailVerificationHandler(this.messageProperties, this.emailVerificationLinkClient, this.authUserClient);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.emailVerificationLinkClient, this.authUserClient, this.messageProperties);
    }

    @Test
    void givenNotification_whenSend_thenIssuesLinkAndVerifies() {
        //given
        final UUID verificationTokenId = UUID.fromString("77777777-7777-7777-7777-777777777777");
        final UUID pepperId = UUID.fromString("88888888-8888-8888-8888-888888888888");
        final Notification<EmailVerifyContent> notification = mock(Notification.class);
        final EmailVerifyContent content = mock(EmailVerifyContent.class);
        final EmailVerificationLink link = mock(EmailVerificationLink.class);

        when(notification.getContent()).thenReturn(content);
        when(content.getVerificationTokenId()).thenReturn(verificationTokenId);
        when(content.getPepperId()).thenReturn(pepperId);
        when(this.emailVerificationLinkClient.issueEmailVerificationLink(verificationTokenId, pepperId))
                .thenReturn(link);

        //when
        this.subject.send(notification);

        //then
        verify(notification).getContent();
        verify(content).getVerificationTokenId();
        verify(content).getPepperId();
        verify(this.emailVerificationLinkClient).issueEmailVerificationLink(verificationTokenId, pepperId);
        verify(this.authUserClient).verifyEmail(link);
        verifyNoMoreInteractions(notification, content, link);
    }
}
