package com.sitionix.ntfssox.domain.client;

import java.util.UUID;

public interface EmailVerificationLinkClient {

    String issueEmailVerificationLink(UUID tokenId, UUID pepperId);
}
