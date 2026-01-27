package com.sitionix.ntfssox.domain.client;

import com.sitionix.ntfssox.domain.model.EmailVerificationLink;

public interface AuthUserClient {

    void verifyEmail(EmailVerificationLink emailVerification);
}
