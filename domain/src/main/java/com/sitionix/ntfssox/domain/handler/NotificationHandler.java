package com.sitionix.ntfssox.domain.handler;

import com.sitionix.ntfssox.domain.model.Notification;

public interface NotificationHandler {

    <C> void send(Notification<C> notification);
}
