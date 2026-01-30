package com.sitionix.ntfssox.domain.model.content;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailVerifyContent {

    private UUID verificationTokenId;

    private UUID pepperId;
}
