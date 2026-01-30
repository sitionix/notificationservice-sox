package com.sitionix.ntfssox.application.usecase;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class VerifyEmailImplTest {

    @Mock
    private AuthUserClient authUserClient;

    private VerifyEmailImpl subject;

    @BeforeEach
    void setUp() {
        this.subject = new VerifyEmailImpl(this.authUserClient);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.authUserClient);
    }

    @Test
    void givenEmailVerificationLink_whenExecute_thenVerifiesEmail() {
        //given
        final EmailVerificationLink emailVerificationLink = mock(EmailVerificationLink.class);

        //when
        this.subject.execute(emailVerificationLink);

        //then
        verify(this.authUserClient).verifyEmail(emailVerificationLink);
        verifyNoMoreInteractions(emailVerificationLink);
    }

    @Test
    void givenNullEmailVerificationLink_whenExecute_thenDoesNothing() {
        //given
        final EmailVerificationLink emailVerificationLink = null;

        //when
        this.subject.execute(emailVerificationLink);

        //then
        verifyNoMoreInteractions(this.authUserClient);
    }
}
