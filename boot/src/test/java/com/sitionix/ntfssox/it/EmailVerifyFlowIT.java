package com.sitionix.ntfssox.it;

import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.forgeit.wiremock.internal.domain.RequestBuilder;
import com.sitionix.ntfssox.it.endpoint.WireMockEndpoint;
import com.sitionix.ntfssox.it.kafka.EmailVerifyKafkaContracts;
import com.sitionix.forgeit.wiremock.api.WireMockQueryParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@IntegrationTest
class EmailVerifyFlowIT {

    @Autowired
    private NotificationForgeItSupport forgeIt;

    @Test
    @DisplayName("Given email verify event When consumed Then authsox client hits WireMock")
    void givenEmailVerifyEvent_whenConsumed_thenAuthsoxClientHitsWireMock() {
        final UUID tokenId = UUID.fromString("11111111-2222-3333-4444-555555555555");
        final UUID pepperId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        final RequestBuilder<?, ?> issueLinkRequest = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.issueEmailVerificationLink(tokenId))
                .urlWithQueryParam(WireMockQueryParams.create().add("pepper", pepperId))
                .responseBody("issueEmailVerificationLinkResponse.json")
                .responseStatus(HttpStatus.OK)
                .create();

        final RequestBuilder<?, ?> requestBuilder = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.verifyEmail())
                .matchesJson("emailVerifyRequest.json")
                .responseBody("emailVerifyResponse.json")
                .responseStatus(HttpStatus.OK)
                .plainUrl()
                .create();

        this.forgeIt.kafka()
                .publish(EmailVerifyKafkaContracts.EMAIL_VERIFY_INPUT)
                .sendAndVerify(e -> {
                    issueLinkRequest.verify();
                    requestBuilder.verify();
                });

    }
}
