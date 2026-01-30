package com.sitionix.ntfssox.domain.config;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.Objects.isNull;

public interface MapperSupport {

    default UUID toUuid(final String value) {
        if (isNull(value)) {
            return null;
        }
        return UUID.fromString(value);
    }

    default Instant toInstant(final OffsetDateTime value) {
        return value != null ? value.toInstant() : null;
    }
}
