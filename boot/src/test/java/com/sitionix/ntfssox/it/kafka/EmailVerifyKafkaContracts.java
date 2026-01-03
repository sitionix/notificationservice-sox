package com.sitionix.ntfssox.it.kafka;

import com.app_afesox.athssox.events.emailverify.EmailVerifyEvent;
import com.app_afesox.athssox.events.emailverify.EmailVerifyEventEnvelope;
import com.app_afesox.athssox.events.kafka.AvroRecordSerializer;
import com.app_afesox.events.Metadata;
import com.sitionix.forgeit.kafka.api.KafkaContract;

public final class EmailVerifyKafkaContracts {

    public static final KafkaContract<EmailVerifyEventEnvelope> EMAIL_VERIFY_INPUT =
            KafkaContract.producerContract()
                    .defaultEnvelope(EmailVerifyEventEnvelope.class)
                    .topic("athssox.it.email-verify.public.v1")
                    .defaultPayload(EmailVerifyEvent.class, "defaultEmailVerifyEvent.json")
                    .defaultMetadata(Metadata.class, "defaultEmailVerifyMetadata.json")
                    .payloadSerializer(AvroRecordSerializer.class)
                    .build();

    private EmailVerifyKafkaContracts() {
    }
}
