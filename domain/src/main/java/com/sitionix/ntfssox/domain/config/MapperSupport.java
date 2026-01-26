package com.sitionix.ntfssox.domain.config;

import java.util.UUID;

import static java.util.Objects.isNull;

public interface MapperSupport {

    default UUID toUuid(final String value) {
        if (isNull(value)) {
            return null;
        }
        return UUID.fromString(value);
    }
}
