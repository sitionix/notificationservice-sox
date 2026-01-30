package com.sitionix.ntfssox.domain.model;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum NotificationTemplate implements NotificationHandler<Object> {

    EMAIL_VERIFY(1L, "emailVerification");

    @Getter
    private final Long id;

    @Getter
    private final String bindingKey;

    @Setter
    private NotificationHandler<?> handler;

    @Override
    @SuppressWarnings("unchecked")
    public void send(final Notification<? extends Object> notification) {
        ((NotificationHandler<Object>) this.handler).send(notification);
    }

    public static NotificationTemplate byBindingKey(final String bindingKey) {
        return Arrays.stream(values())
                .filter(template -> template.bindingKey.equals(bindingKey))
                .findFirst()
                .orElse(null);
    }
}
