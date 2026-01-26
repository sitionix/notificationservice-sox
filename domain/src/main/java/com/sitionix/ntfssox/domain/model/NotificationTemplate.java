package com.sitionix.ntfssox.domain.model;

import com.sitionix.ntfssox.domain.handler.NotificationHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@RequiredArgsConstructor
public enum NotificationTemplate implements NotificationHandler {

    EMAIL_VERIFY(1L, "email-verification");

    @Getter
    private final Long id;

    @Getter
    private final String bindingKey;

    @Setter
    private NotificationHandler handler;

    @Override
    public <C> void send(Notification<C> notification) {
        this.handler.send(notification);
    }
}
