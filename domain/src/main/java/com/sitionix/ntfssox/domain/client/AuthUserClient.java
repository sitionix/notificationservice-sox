package com.sitionix.ntfssox.domain.client;

import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;

public interface AuthUserClient {

    void verifyEmail(EmailVerifyPayload payload);
}
