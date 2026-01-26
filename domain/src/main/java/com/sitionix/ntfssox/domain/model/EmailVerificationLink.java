package com.sitionix.ntfssox.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EmailVerificationLink {
    private String token;
    private UUID tokenId;
    private UUID siteId;
    private Instant expiresAt;
}
