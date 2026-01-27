package com.sitionix.ntfssox.domain.model;

public sealed interface NotificationHandler<C>
        permits AbstractNotificationHandler, NotificationTemplate {

    void send(Notification<? extends C> notification);
}
