package com.sitionix.ntfssox.domain.usecase;

import com.sitionix.ntfssox.domain.model.EmailVerificationLink;

public interface VerifyEmail {

    void execute(EmailVerificationLink emailVerification);
}
