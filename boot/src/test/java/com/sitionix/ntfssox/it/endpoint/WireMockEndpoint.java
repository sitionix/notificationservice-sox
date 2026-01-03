package com.sitionix.ntfssox.it.endpoint;

import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.app_afesox.bffssox.client.dto.EmailVerificationResponseDTO;
import com.sitionix.forgeit.domain.endpoint.Endpoint;
import com.sitionix.forgeit.domain.endpoint.HttpMethod;

public final class WireMockEndpoint {

    private WireMockEndpoint() {
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
