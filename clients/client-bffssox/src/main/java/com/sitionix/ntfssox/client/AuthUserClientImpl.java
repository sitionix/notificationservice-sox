package com.sitionix.ntfssox.client;

import com.app_afesox.bffssox.client.api.AuthApi;
import com.sitionix.ntfssox.domain.client.AuthUserClient;
import com.sitionix.ntfssox.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserClientImpl implements AuthUserClient {

    private final AuthApi authApi;

    @Override
    public void verifyEmail(final Notification payload) {
//        if (request == null || request.getToken() == null || request.getToken().isBlank()) {
//            log.warn("Email verification token not resolved for tokenId={}", tokenId);
//            return;
//        }
//        this.authApi.verifyEmail(request);
    }

}
