package com.sitionix.ntfssox.domain.usecase;

import com.sitionix.ntfssox.domain.model.Notification;

public interface VerifyEmail {

    void execute(Notification payload);
}
