package com.sitionix.ntfssox.domain.model;

public abstract non-sealed class AbstractNotificationHandler<C> implements NotificationHandler<C> {

    protected final MessageProperties props;

    protected AbstractNotificationHandler(final MessageProperties props) {
        this.props = props;
    }

}
