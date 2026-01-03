package com.sitionix.ntfssox.domain.usecase;

import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;

public interface VerifyEmail {

    void execute(EmailVerifyPayload payload);
}
