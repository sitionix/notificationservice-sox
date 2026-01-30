package com.sitionix.ntfssox.it;

import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.forgeit.wiremock.api.WireMockPathParams;
import com.sitionix.forgeit.wiremock.api.WireMockQueryParams;
import com.sitionix.forgeit.wiremock.internal.domain.RequestBuilder;
import com.sitionix.ntfssox.it.endpoint.WireMockEndpoint;
import com.sitionix.ntfssox.it.kafka.EmailVerifyKafkaContracts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.sitionix.forgeit.wiremock.api.Parameter.equalTo;

@IntegrationTest
class NotificationConsumerErrorHandlingIT {

    @Autowired
    private NotificationForgeItSupport forgeIt;

    @Test
    @DisplayName("given failing email verify event when consumed then next message processed")
    void givenFailingEmailVerifyEvent_whenConsumed_thenNextMessageProcessed() {
        //given
        final RequestBuilder<?, ?> failingIssueLinkRequest = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.issueEmailVerificationLink())
                .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .pathPattern(WireMockPathParams.create().add("id", equalTo("1bc623d6-1242-4e37-b576-eb73ed9c88f6")))
                .urlWithQueryParam(WireMockQueryParams.create().add("pepper", equalTo("81aaab7e-f6e5-4ff7-bb06-39d5d857de62")))
                .create();

        final RequestBuilder<?, ?> successIssueLinkRequest = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.issueEmailVerificationLink())
                .responseBody("issueEmailVerificationLinkResponse.json")
                .responseStatus(HttpStatus.OK)
                .pathPattern(WireMockPathParams.create().add("id", equalTo("2f9d14e1-0f7d-4c32-b0ef-2f5a79c68d4a")))
                .urlWithQueryParam(WireMockQueryParams.create().add("pepper", equalTo("b2a55ab5-fc43-41e2-97c0-620f9bd29e1a")))
                .create();

        final RequestBuilder<?, ?> verifyEmailRequest = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.verifyEmail())
                .responseBody("emailVerifyResponse.json")
                .responseStatus(HttpStatus.OK)
                .plainUrl()
                .create();

        //when
        this.forgeIt.kafka()
                .publish(EmailVerifyKafkaContracts.EMAIL_VERIFY_INPUT)
                .payload("givenPayloadWithSiteIdNull.json")
                .sendAndVerify(result -> failingIssueLinkRequest.verify());

        this.forgeIt.kafka()
                .publish(EmailVerifyKafkaContracts.EMAIL_VERIFY_INPUT)
                .payload("givenPayloadWithSiteIdNullSecond.json")
                .sendAndVerify(result -> {
                    successIssueLinkRequest.verify();
                    verifyEmailRequest.verify();
                });
    }
}
