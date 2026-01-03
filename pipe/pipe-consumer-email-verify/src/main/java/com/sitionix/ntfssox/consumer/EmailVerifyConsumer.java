package com.sitionix.ntfssox.consumer;

import com.app_afesox.athssox.events.emailverify.EmailVerifyEventEnvelope;
import com.app_afesox.athssox.events.emailverify.kafka.EmailverifyV1ConsumerHandler;
import com.sitionix.ntfssox.consumer.mapper.EmailVerifyEventMapper;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import com.sitionix.ntfssox.domain.usecase.VerifyEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerifyConsumer implements EmailverifyV1ConsumerHandler {

    private final EmailVerifyEventMapper mapper;

    private final VerifyEmail verifyEmail;

    @Override
    public void consumeEmailVerify(final EmailVerifyEventEnvelope emailVerifyEventEnvelope) {
        final EmailVerifyPayload payload = this.mapper.asPayload(emailVerifyEventEnvelope.getPayload());
        this.verifyEmail.execute(payload);
    }
}
