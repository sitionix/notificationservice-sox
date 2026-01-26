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

import java.util.UUID;

import static com.sitionix.forgeit.wiremock.api.Parameter.equalTo;

@IntegrationTest
class EmailVerifyFlowIT {

    @Autowired
    private NotificationForgeItSupport forgeIt;

    @Test
    @DisplayName("given email verify event when consumed then verify email")
    void givenEmailVerifyEvent_whenConsumed_thenVerifyEmail() {
        //given
        final RequestBuilder<?, ?> issueLinkRequest = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.issueEmailVerificationLink())
                .responseBody("issueEmailVerificationLinkResponse.json")
                .responseStatus(HttpStatus.OK)
                .pathPattern(WireMockPathParams.create().add("id", equalTo("1bc623d6-1242-4e37-b576-eb73ed9c88f6")))
                .urlWithQueryParam(WireMockQueryParams.create().add("pepper", equalTo("81aaab7e-f6e5-4ff7-bb06-39d5d857de62")))
                .create();

        //when
        this.forgeIt.kafka()
                .publish(EmailVerifyKafkaContracts.EMAIL_VERIFY_INPUT)
                .payload("givenPayloadWithSiteIdNull.json")
                .sendAndVerify(p -> issueLinkRequest.verify());
    }


    @Test
    @DisplayName("Given email verify event When consumed Then authsox link + bffssox verify hit WireMock")
    void givenEmailVerifyEvent_whenConsumed_thenAuthsoxLinkAndBffssoxVerifyHitWireMock() {
        final UUID tokenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        final UUID pepperId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        final RequestBuilder<?, ?> issueLinkRequest = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.issueEmailVerificationLink())
                .responseBody("issueEmailVerificationLinkResponse.json")
                .responseStatus(HttpStatus.OK)
                .pathPattern(WireMockPathParams.create().add("id", equalTo(tokenId.toString())))
                .urlWithQueryParam(WireMockQueryParams.create().add("pepper", equalTo(pepperId.toString())))
                .create();

        final RequestBuilder<?, ?> requestBuilder = this.forgeIt.wiremock()
                .createMapping(WireMockEndpoint.verifyEmail())
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
