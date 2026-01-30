package com.sitionix.ntfssox.domain.client;

import com.sitionix.ntfssox.domain.model.EmailVerificationLink;
import java.util.UUID;

public interface EmailVerificationLinkClient {

    EmailVerificationLink issueEmailVerificationLink(UUID tokenId, UUID pepperId);
}
