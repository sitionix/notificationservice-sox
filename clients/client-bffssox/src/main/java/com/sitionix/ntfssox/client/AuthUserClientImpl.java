package com.sitionix.ntfssox.client;

import com.app_afesox.bffssox.client.api.AuthApi;
import com.app_afesox.bffssox.client.dto.EmailVerificationDTO;
import com.sitionix.ntfssox.client.mapper.EmailVerifyClientMapper;
import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.model.EmailVerifyPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserClientImpl implements AuthUserClient {

    private final AuthApi authApi;

    private final EmailVerifyClientMapper emailVerifyClientMapper;

    @Override
    public void verifyEmail(final EmailVerifyPayload payload) {
        final EmailVerificationDTO request = this.emailVerifyClientMapper.asEmailVerificationDto(payload);
        this.authApi.verifyEmail(request);
    }
}
