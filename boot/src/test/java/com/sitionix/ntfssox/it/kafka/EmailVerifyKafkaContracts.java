package com.sitionix.ntfssox.it.kafka;

import com.app_afesox.ntfssox.events.kafka.AvroRecordSerializer;
import com.app_afesox.events.Metadata;
import com.app_afesox.ntfssox.events.notifications.NotificationEnvelope;
import com.app_afesox.ntfssox.events.notifications.NotificationEvent;
import com.sitionix.forgeit.kafka.api.KafkaContract;

public final class EmailVerifyKafkaContracts {

    public static final KafkaContract<NotificationEnvelope> EMAIL_VERIFY_INPUT =
            KafkaContract.producerContract()
                    .defaultEnvelope(NotificationEnvelope.class)
                    .topic("ntfssox.it.notification.public.unified.v1")
                    .defaultPayload(NotificationEvent.class, "defaultEmailVerifyEvent.json")
                    .defaultMetadata(Metadata.class, "defaultEmailVerifyMetadata.json")
                    .payloadSerializer(AvroRecordSerializer.class)
                    .build();

    private EmailVerifyKafkaContracts() {
    }
}
