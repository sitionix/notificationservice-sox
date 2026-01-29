package com.sitionix.ntfssox.it;

import com.sitionix.forgeit.core.test.IntegrationTest;
import com.sitionix.forgeit.wiremock.api.WireMockPathParams;
import com.sitionix.forgeit.wiremock.api.WireMockQueryParams;
import com.sitionix.forgeit.wiremock.internal.domain.RequestBuilder;
import com.sitionix.ntfssox.it.endpoint.WireMockEndpoint;
import com.sitionix.ntfssox.it.kafka.EmailVerifyKafkaContracts;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.sitionix.forgeit.wiremock.api.Parameter.equalTo;

@IntegrationTest
@Execution(ExecutionMode.SAME_THREAD)
@Slf4j
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
                .sendAndVerify(p -> {
                    logRequestBuilderEndpoint(issueLinkRequest, "issueLinkRequest");
                    logRequestBuilderEndpoint(verifyEmailRequest, "verifyEmailRequest");
                    logRequestCounts();
                    issueLinkRequest.verify();
                    verifyEmailRequest.verify();
                });
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
                    logRequestBuilderEndpoint(issueLinkRequest, "issueLinkRequest");
                    logRequestBuilderEndpoint(requestBuilder, "verifyEmailRequest");
                    logRequestCounts();
                    issueLinkRequest.verify();
                    requestBuilder.verify();
                });

    }

    private void logRequestCounts() {
        try {
            final Object journal = this.forgeIt.wiremock();
            final java.lang.reflect.Field clientField = journal.getClass().getDeclaredField("journalClient");
            clientField.setAccessible(true);
            final Object client = clientField.get(journal);
            final java.lang.reflect.Method findBodies = client.getClass()
                    .getMethod("findBodiesByUrl", com.sitionix.forgeit.domain.endpoint.Endpoint.class);
            final com.sitionix.forgeit.domain.endpoint.Endpoint<?, ?> issueEndpoint =
                    WireMockEndpoint.issueEmailVerificationLink();
            final com.sitionix.forgeit.domain.endpoint.Endpoint<?, ?> verifyEndpoint =
                    WireMockEndpoint.verifyEmail();
            log.info("WireMock endpoints: issueLink method={}, template={}, hasQuery={}, verifyEmail method={}, template={}, hasQuery={}",
                    issueEndpoint.getMethod(),
                    issueEndpoint.getUrlBuilder().getTemplate(),
                    issueEndpoint.getUrlBuilder().hasQueryParameters(),
                    verifyEndpoint.getMethod(),
                    verifyEndpoint.getUrlBuilder().getTemplate(),
                    verifyEndpoint.getUrlBuilder().hasQueryParameters());
            final java.util.List<?> issueLinkBodies =
                    (java.util.List<?>) findBodies.invoke(client, issueEndpoint);
            final java.util.List<?> verifyEmailBodies =
                    (java.util.List<?>) findBodies.invoke(client, verifyEndpoint);
            final int issueLinkCount = issueLinkBodies.size();
            final int verifyEmailCount = verifyEmailBodies.size();
            final int issueLinkDistinct = new java.util.HashSet<>(issueLinkBodies).size();
            final int verifyEmailDistinct = new java.util.HashSet<>(verifyEmailBodies).size();
            final String wiremockBaseUrl = System.getProperty("forge-it.wiremock.base-url");
            log.info("WireMock counts: issueLink={}, verifyEmail={}, issueLinkDistinct={}, verifyEmailDistinct={}, baseUrl={}",
                    issueLinkCount, verifyEmailCount, issueLinkDistinct, verifyEmailDistinct, wiremockBaseUrl);
            log.info("WireMock issueLink bodies: {}", issueLinkBodies);
            log.info("WireMock verifyEmail bodies: {}", verifyEmailBodies);
        } catch (Exception ex) {
            log.info("WireMock counts: failed to read journal ({})", ex.getClass().getSimpleName());
        }
    }

    private void logRequestBuilderEndpoint(final RequestBuilder<?, ?> requestBuilder, final String name) {
        try {
            final java.lang.reflect.Field endpointField = requestBuilder.getClass().getDeclaredField("endpoint");
            endpointField.setAccessible(true);
            final com.sitionix.forgeit.domain.endpoint.Endpoint<?, ?> endpoint =
                    (com.sitionix.forgeit.domain.endpoint.Endpoint<?, ?>) endpointField.get(requestBuilder);
            log.info("WireMock requestBuilder {}: method={}, template={}, hasQuery={}",
                    name,
                    endpoint.getMethod(),
                    endpoint.getUrlBuilder().getTemplate(),
                    endpoint.getUrlBuilder().hasQueryParameters());
        } catch (Exception ex) {
            log.info("WireMock requestBuilder {}: failed to read endpoint ({})",
                    name, ex.getClass().getSimpleName());
        }
    }
}
