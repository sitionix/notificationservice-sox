package com.sitionix.ntfssox.application.usecase;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import com.sitionix.ntfssox.domain.usecase.VerifyEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyEmailImpl implements VerifyEmail {

    private final AuthUserClient authUserClient;

    @Override
    public void execute(final EmailVerificationLink emailVerification) {
        if (emailVerification == null) {
            log.warn("Email verification is null");
            return;
        }
        this.authUserClient.verifyEmail(emailVerification);
    }
}
