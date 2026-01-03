package com.sitionix.ntfssox.application.usecase;

import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import com.sitionix.ntfssox.domain.usecase.VerifyEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailImpl implements VerifyEmail {

    private final AuthUserClient authUserClient;

    @Override
    public void execute(final EmailVerifyPayload payload) {
        this.authUserClient.verifyEmail(payload);
    }
}
