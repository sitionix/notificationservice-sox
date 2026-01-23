package com.sitionix.ntfssox.it.endpoint;

import com.app_afesox.athssox.client.dto.EmailVerificationDTO;
import com.app_afesox.athssox.client.dto.EmailVerificationResponseDTO;
import com.app_afesox.athssox.client.dto.IssueEmailVerificationLinkResponse;
import com.sitionix.forgeit.domain.endpoint.Endpoint;
import com.sitionix.forgeit.domain.endpoint.HttpMethod;
import java.util.UUID;

public final class WireMockEndpoint {

    private WireMockEndpoint() {
    }

    public static Endpoint<Void, IssueEmailVerificationLinkResponse> issueEmailVerificationLink(final UUID tokenId) {
        return Endpoint.createContract(
                "/authsox/api/v1/auth/emailVerificationTokens/" + tokenId + ":issueLink",
                HttpMethod.GET,
                Void.class,
                IssueEmailVerificationLinkResponse.class
        );
    }

    public static Endpoint<EmailVerificationDTO, EmailVerificationResponseDTO> verifyEmail() {
        return Endpoint.createContract(
                "/api/v1/auth/email/verify",
                HttpMethod.POST,
                EmailVerificationDTO.class,
                EmailVerificationResponseDTO.class
        );
    }
}
