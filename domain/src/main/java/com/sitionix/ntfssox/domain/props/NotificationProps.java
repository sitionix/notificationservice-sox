package com.sitionix.ntfssox.domain.props;

import java.util.List;

public interface NotificationProps {

    Messages getMessages();

    interface Messages {
        EmailVerification getEmailVerification();
    }

    interface EmailVerification {
        List<String> getAllowedChannels();
        Delivery getDelivery();
    }

    interface Delivery {
        EmailDelivery getEmail();
    }

    interface EmailDelivery {
        Link getLink();
    }

    interface Link {
        String getUrl();
    }
}
