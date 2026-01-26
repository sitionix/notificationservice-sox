package com.sitionix.ntfssox.domain.client;

import com.sitionix.ntfssox.domain.model.Notification;

public interface AuthUserClient {

    void verifyEmail(Notification payload);
}
