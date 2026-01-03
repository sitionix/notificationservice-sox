package com.sitionix.ntfssox.it;

import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.forgeit.wiremock.internal.domain.RequestBuilder;
import com.sitionix.ntfssox.it.endpoint.WireMockEndpoint;
import com.sitionix.ntfssox.it.kafka.EmailVerifyKafkaContracts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@IntegrationTest
class EmailVerifyFlowIT {

    @Autowired
    private NotificationForgeItSupport forgeIt;

    @Test
    @DisplayName("Given email verify event When consumed Then bffssox client hits WireMock")
    void givenEmailVerifyEvent_whenConsumed_thenBffssoxClientHitsWireMock() {
        final RequestBuilder<?, ?> requestBuilder = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.verifyEmail())
                .matchesJson("emailVerifyRequest.json")
                .responseBody("emailVerifyResponse.json")
                .responseStatus(HttpStatus.OK)
                .plainUrl()
                .create();

        this.forgeIt.kafka()
                .publish(EmailVerifyKafkaContracts.EMAIL_VERIFY_INPUT)
                .sendAndVerify(e -> requestBuilder.verify());

    }
}
